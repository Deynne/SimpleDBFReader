package com.dyn.dbf.model;

import java.util.List;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * O cabecalho do arquivo dbf sendo lido
 * @author Deynne Silva
 * @version 1.0
 */
public class CabecalhoDbf {

	// A explicação para estes campos do cabeçalho podem ser encontradas no seguinte link: https://en.wikipedia.org/wiki/.dbf
	private byte tipoDeDbf; // byte 0
	private byte ano; // byte 1
	private byte mes; // byte 2
	private byte dia; // byte 3
	
	// dbf usa little endian (bytes menos significativos primeiro), precisa converter para big endian 
	// (bytes mais significativos primeiro)
	private int numeroDeRegistros; // byte 3-7 
	
	private short tamanhoHeader; // byte 8-9
	
	// inclui flag de deletado
	private short tamanhoDoRegistro; // byte 10-11
	
	private byte reservado1[] = new byte[2]; // byte 12-13
	private byte flagDeTransacao; // byte 14
	private byte flagEncriptacao; // byte 15
	private byte reservadoDOS[] = new byte[12]; // byte 16-27
	private byte flagMdx; // byte 28
	private byte idDriverDeLinguagem; // byte 29
	private byte reservado2[] = new byte[2]; // byte 30-13
	
	private CampoDbf [] campos; // byte 32-n {n | 0 <= n <= 255} (cada campo tem 32 bytes);
	
	
	private String [] nome_campos;
	private static final byte tamanhoCampo = 32;
	public static final byte caracterDeTermino = 0x0D; // caracter de termino do array de campo
	
	/**
	 * Contrutor basico do cabecalho.
	 */
	public CabecalhoDbf() {	}
	
	
	/**
	 * Um construtor para o cabeçalho do dbf.
	 * O padrão de montagem do cabeçalho é feito seguindo o formato definido para os arquivos dbf.
	 * @param dbfStream O Stream do arquivo dbf sendo lido.
	 */
	public void buildCabecalho(DataInputStream dbfStream, Charset charset) {
		if(dbfStream == null) return;
		
		try {
			tipoDeDbf = dbfStream.readByte();
			ano = dbfStream.readByte();
			mes = dbfStream.readByte();
			dia = dbfStream.readByte();
			
			// reverte por que precisa converter de little endian pra big endian
			numeroDeRegistros = Integer.reverseBytes(dbfStream.readInt()); 
			tamanhoHeader = Short.reverseBytes(dbfStream.readShort());
			tamanhoDoRegistro = Short.reverseBytes(dbfStream.readShort());
			
			this.nome_campos = new String[(tamanhoHeader-CabecalhoDbf.tamanhoCampo)/CabecalhoDbf.tamanhoCampo]; // Cada campo tem 32 bytes
			int i = 0;
			
			for(i = 0; i < reservado1.length;i++) {
				reservado1[i] = dbfStream.readByte();
			}
			
			flagDeTransacao = dbfStream.readByte();
			flagEncriptacao = dbfStream.readByte();
			
			for(i = 0; i < reservadoDOS.length;i++) {
				reservadoDOS[i] = dbfStream.readByte();
			}
			
			flagMdx = dbfStream.readByte();
			idDriverDeLinguagem = dbfStream.readByte();
			
			for(i = 0; i < reservado2.length;i++) {
				reservado2[i] = dbfStream.readByte();
			}
			// Cada campo tem 32 bytes e o tamanho do header é em bytes
			instanciaCampos(dbfStream, charset);
			dbfStream.readByte(); // caracter de termino
//			dbfStream.readShort()
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Realiza a instância dos campos presentes dentro do dbf.
	 * @param dbfStream A stream do dbf sendo lido.
	 */
	private void instanciaCampos(DataInputStream dbfStream,Charset charset) {
		List<CampoDbf> listaDeCampos = new ArrayList<CampoDbf>();
		
		final int tamanhoDoCampo = CabecalhoDbf.tamanhoCampo;
		int lendo = tamanhoDoCampo; 
		int i = 0;
		while(lendo <= this.tamanhoHeader - tamanhoDoCampo) {
			CampoDbf campo = CampoDbf.buildCampo(dbfStream, charset);
			
			listaDeCampos.add(campo);
			this.nome_campos[i++] = campo.getNome();
			
			lendo += tamanhoDoCampo;
		}
		this.campos = listaDeCampos.toArray(new CampoDbf[listaDeCampos.size()]);
	}

	/**
	 * Retorna o tipo de dbf. Ver documentação do arquivo dbf para entender como diferenciar os tipos.
	 * @return Um <b>byte</b> que representa o tipo do dbf 
	 */
	public byte getTipoDeDbf() {
		return tipoDeDbf;
	}

	/**
	 * Obtêm o ano da ultima atualização
	 * @return Um <b>byte</b> representando o ano da ultima atualização
	 */
	public byte getAno() {
		return ano;
	}
	/**
	 * Obtêm o mês da ultima atualização
	 * @return Um <b>byte</b> representando o mês da ultima atualização
	 */
	public byte getMes() {
		return mes;
	}
	/**
	 * Obtêm o dia da ultima atualização
	 * @return Um <b>byte</b> representando o dia da ultima atualização
	 */
	public byte getDia() {
		return dia;
	}
	/**
	 * Obtêm o numero de registros presentes no dbf
	 * @return Um <b>int</b> com o valor da quantidade de registros no arquivo 
	 */
	public int getNumeroDeRegistros() {
		return numeroDeRegistros;
	}

	/**
	 * A quantidade de bytes no header
	 * @return Um <b>short</b> com o valor da quantidade de bytes presente no header
	 */
	public short getTamanhoHeader() {
		return tamanhoHeader;
	}

	/**
	 * O tamanho de uma linha de dados no arquivo. Inclui o byte de verificação de deleção.
	 * @return Um <b>byte</b> com o valor da quantidade de bytes em uma linha de dados no arquivo dbf.
	 */
	public short getTamanhoDoRegistro() {
		return tamanhoDoRegistro;
	}

	public byte[] getReservado1() {
		return reservado1;
	}

	public byte getFlagDeTransacao() {
		return flagDeTransacao;
	}

	public byte getFlagEncriptacao() {
		return flagEncriptacao;
	}

	public byte[] getReservadoDOS() {
		return reservadoDOS;
	}

	public byte getFlagMdx() {
		return flagMdx;
	}

	public byte getIdDriverDeLinguagem() {
		return idDriverDeLinguagem;
	}


	public byte[] getReservado2() {
		return reservado2;
	}


	/**
	 * A lista de campos presentes no arquivo.
	 * @return Um {@link CampoDbf}[ ] com a lista de campos presentes no arquivo dbf.
	 */
	public CampoDbf[] getCampos() {
		return campos;
	}

	/**
	 * A quantidade de campos presentes no arquivo dbf
	 * @return um <b>int</b> contendo os campos presentes no dbf.
	 */
	public int getNumCampos() {
		return this.campos.length;
	}
	
	/**
	 * <p>
	 *   Retorna o indice de um campo de acordo com parte do nome dele.
	 * </p>
	 * <p>
	 *   A busca é realizada em ambos os sentidos (inicio-fim e fim-inicio) simultâneamete, mas prioriza os campos próximos do início da lista.
	 * </p>
	 * @param nomeCampo Nome do campo que se deseja encontrar
	 * @return O indice do campo buscado ou -1 se nenhum campo for encontrado
	 */
	public int getIdByNome(String nomeCampo) {
		for(int i = 0, j = this.nome_campos.length - 1; i <= this.nome_campos.length/2;i++,j--) {
			if(this.nome_campos[i].trim().equals(nomeCampo)) {
				return i;
			}
			else if(this.nome_campos[j].trim().equals(nomeCampo)) {
				return j;
			}
		}
		return -1;
	}
	
	/**
	 * O nome do campo de acordo com o indice
	 * @param index o indice do campo.
	 * @return Uma {@link String} com o nome do campo identificado no indice. <b>null</b> para indices invalidos.
	 */
	public String getNomeById(int indice) {
		if(indice >= this.nome_campos.length || indice < 0) return null;
		return this.nome_campos[indice];
	}

	@Override
	public String toString() {
		return "CabecalhoDbf [ tipoDeDbf=" + tipoDeDbf + ", ano=" + ano + ", mes=" + mes
				+ ", dia=" + dia + ", numeroDeRegistros=" + numeroDeRegistros + ", tamanhoHeader=" + tamanhoHeader
				+ ", tamanhoDoRegistro=" + tamanhoDoRegistro + ", reservado1=" + Arrays.toString(reservado1)
				+ ", flagDeTransacao=" + flagDeTransacao + ", flagEncriptacao=" + flagEncriptacao + ", reservadoDOS="
				+ Arrays.toString(reservadoDOS) + ", flagMdx=" + flagMdx + ", idDriverDeLinguagem="
				+ idDriverDeLinguagem + ", reservado2=" + Arrays.toString(reservado2) + ", campos="
				+ Arrays.toString(campos) + " ]\n";
	}

	
	
	
}
