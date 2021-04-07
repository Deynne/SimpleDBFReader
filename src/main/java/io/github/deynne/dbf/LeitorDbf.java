package io.github.deynne.dbf;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.github.deynne.dbf.exceptions.LeituraIncorretaDeCampoException;
import io.github.deynne.dbf.model.CabecalhoDbf;
import io.github.deynne.dbf.model.Campo;
import io.github.deynne.dbf.model.CampoDbf;
import io.github.deynne.dbf.model.Linha;

/**
 * Um leitor simplificado para arquivos dbf. A partir dele � poss�vel ler o arquivo e todas as linhas contidas nele como um {@link Campo}, assim como obter os dados
 * no formato de {@link String}.
 * @author Deynne Silva
 * @version 1.0
 */
public class LeitorDbf implements Closeable{

	private InputStream inputStream;
	private DataInputStream dbfStream;
	/**
	 * O charset a ser utilizado na convers�o dos dados para string.
	 */
	private Charset charset;
	private CabecalhoDbf cabecalho;
	private int recordsLidos;
	
	private enum CaracterInicial {
		DADO_DELETADO((byte)0x2A), // equivale ao caracter '*'
		DADO_PRESENTE((byte)0x20); // equivale ao caracter ' '
		
		private byte value;
		
		CaracterInicial(byte v) {
			value = v;
		}
		
		public byte getValue() {
			return value;
		}
	}
	
	/**
	 * <p>
	 * 	Construtor baseado em objetos do tipo {@link File}. O {@link File} � utilizado para criar uma stream de dados.
	 * </p>
	 * <p>
	 * 	O charset padr�o � {@link StandardCharsets#UTF_8}.
	 * </p>
	 * @param file Um file indicando o arquivo dbf a ser lido
	 * @throws FileNotFoundException Se o arquivo n�o existir.
	 * 
	 * @see #LeitorDbf(File, Charset)
	 * @see #LeitorDbf(InputStream)
	 * @see #LeitorDbf(InputStream, Charset)
	 */
	public LeitorDbf(File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}

	/**
	 * <p>
	 * 	Construtor baseado em stream de dados.
	 * </p>
	 * <p>
	 * 	O charset padr�o � {@link StandardCharsets#UTF_8}.
	 * </p>
	 * @param stream A stream do arquivo dbf que ser� lido.
	 * 
	 * @see #LeitorDbf(File, Charset)
	 * @see #LeitorDbf(File)
	 * @see #LeitorDbf(InputStream, Charset)
	 */
	public LeitorDbf(InputStream stream) {
		this(stream,StandardCharsets.UTF_8);
	}
	
	/**
	 * <p>
	 * 	Construtor baseado em objetos do tipo {@link File}. O {@link File} � utilizado para criar uma stream de dados.
	 * </p>
	 * @param file Um file indicando o arquivo dbf a ser lido
	 * @param charset O charset a ser utilizado na convers�o dos dados para string.
	 * @throws FileNotFoundException Caso o arquivo n�o exista.
	 * 
	 * @see #LeitorDbf(InputStream, Charset)
	 * @see #LeitorDbf(InputStream)
	 * @see #LeitorDbf(File)
	 */
	public LeitorDbf(File file, Charset charset) throws FileNotFoundException {
		this(new FileInputStream(file),charset);
	}

	/**
	 * <p>
	 * 	Construtor baseado em stream de dados.
	 * </p>
	 * @param stream A stream do arquivo dbf que ser� lido.
	 * @param charset O charset a ser utilizado na convers�o dos dados para string.
	 * 
	 * @see #LeitorDbf(File, Charset)
	 * @see #LeitorDbf(InputStream)
	 * @see #LeitorDbf(File)
	 */
	public LeitorDbf(InputStream stream, Charset charset) {
		inputStream = stream;
		dbfStream = new DataInputStream(stream);
		this.charset = charset;
		
		cabecalho = new CabecalhoDbf();
		cabecalho.buildCabecalho(dbfStream,charset);
	}
	
	
	/**
	 * Fecha a os streams de dados
	 * @throws IOException Se ocorrer problemas ao fechar o stream de dados
	 */
	public void close() throws IOException {
		dbfStream.close();
		inputStream.close();
	}

	/**
	 *  Obtem a stream do arquivo dbf
	 * @return Stream de dados utilizada para ler o dbf.
	 */
	public DataInputStream getDbfStream() {
		return dbfStream;
	}

	/**
	 * Obtem o charset utilizado para cria��o das strings lidas a partir do arquivo
	 * @return o charset definido para a leitura dos dados como string.
	 */
	public Charset getCharSet() {
		return charset;
	}

	/**
	 * Obt�m o {@link CabecalhoDbf} do arquivo sendo lido
	 * @return retorna um {@link CabecalhoDbf} contendo informa��es de cabe�alho do arquivo dbf sendo lido.
	 */
	public CabecalhoDbf getCabecalho() {
		return cabecalho;
	}
	
	/**
	 * <p>
	 *  Recupera a pr�xima linha do arquivo de registro.
	 * </p> 
	 * @return Uma {@link Linha} contendo todas as informa��es da linha lida ou <b>null</b> caso n�o exista mais linha para ser lida.
	 * @throws LeituraIncorretaDeCampoException Caso a leitura de um campo seja feita de forma incorreta
	 */
	public Linha proximosRegistros() throws LeituraIncorretaDeCampoException {
		// Se Por algum motivo houveram caracteres sobrando ap�s o cabe�alho, pula eles. Em geral vai pular apenas o caracter de "presente ou deletado"
		try {
			byte temp_b;
			do {
				temp_b = dbfStream.readByte();
				// Pode ser que ja tenha terminado o arquivo, nesse caso retorna para indicar que n�o h� mais nada para ler
				if(temp_b == CabecalhoDbf.caracterDeTermino) 
					return null;
//				else if(temp_b == CaracterInicial.DADO_DELETADO.getValue()) dbfStream.skip(cabecalho.getTamanhoDoRegistro()-1);
				
			}while(temp_b != CaracterInicial.DADO_PRESENTE.getValue() && temp_b != CaracterInicial.DADO_DELETADO.getValue());
		} catch(EOFException e) {
			return null; // Em caso de leitura posterior ao caracter de termino, retornar� null para indicar fim do arquivo
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// A partir daqui � segudo dizer que h� uma linha para ser lida e n�o haver� problema na leitura. Ent�o incrementa o contador de registros lidos
		Linha listaDeRegistros = new Linha(charset);
		recordsLidos++;
		try {
			
			// Realiza a leitura de cada coluna e adiciona a lista de registros
			for(int i = 0; i < cabecalho.getNumCampos();i++) {
				listaDeRegistros.getColunas().add(new Campo(cabecalho.getCampos()[i].getNome(),getValorCampo(cabecalho.getCampos()[i]),cabecalho.getCampos()[i].getTipo(),charset));
			}
		} catch (LeituraIncorretaDeCampoException e) {
			throw new LeituraIncorretaDeCampoException("Erro de leitura de campo na linha " + recordsLidos + ".", e);
		}
		
		return listaDeRegistros;
	}
	
	/**
	 * L� o campo definido no par�metro para a linha atualmente sendo lida no arquivo dbf.
	 * @param campoDbf O campo do qual se espera obter o valor
	 * @return Um array de <b>bytes</b> contendo os dados do campo.
	 * @throws LeituraIncorretaDeCampoException caso o numero de bytes lidos para o campo seja diferente do n�mero de bytes presentes no campo.
	 */
	private byte[] getValorCampo(CampoDbf campoDbf) throws LeituraIncorretaDeCampoException {
		try {
		int bytesLidos = 0;
		byte byteArray[] = new byte[campoDbf.getTamanhoDoCampo()];
		
			bytesLidos += dbfStream.read(byteArray);
			
			if(bytesLidos < campoDbf.getTamanhoDoCampo()) throw new LeituraIncorretaDeCampoException("Foram lidos " + bytesLidos + " bytes do campo " + campoDbf.getNome() + ", mas era esperada a leitura de " + campoDbf.getTamanhoDoCampo() + " bytes.");
			return byteArray;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 
	 * @return Um <b>int</b> representando o n�mero de linhas lidas no arquivo.
	 */
	public int getRecordsLidos() {
		return recordsLidos;
	}

}
