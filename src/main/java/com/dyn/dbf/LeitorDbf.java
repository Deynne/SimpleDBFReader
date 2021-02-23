package com.dyn.dbf;

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
import java.util.ArrayList;
import java.util.List;

import com.dyn.dbf.model.CabecalhoDbf;
import com.dyn.dbf.model.Campo;
import com.dyn.dbf.model.CampoDbf;

public class LeitorDbf implements Closeable{

	private InputStream inputStream;
	private DataInputStream dbfStream;
	private Charset charSet;
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
	
	public LeitorDbf(File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}

	public LeitorDbf(InputStream stream) {
		this(stream,StandardCharsets.UTF_8);
	}
	
	public LeitorDbf(File file, Charset charSet) throws FileNotFoundException {
		this(new FileInputStream(file),charSet);
	}

	public LeitorDbf(InputStream stream, Charset charSet) {
		inputStream = stream;
		dbfStream = new DataInputStream(stream);
		this.charSet = charSet;
		
		cabecalho = new CabecalhoDbf(charSet);
		cabecalho.buildCabecalho(dbfStream);
	}
	
	public void close() throws IOException {
		dbfStream.close();
		inputStream.close();
	}

	public DataInputStream getDbfStream() {
		return dbfStream;
	}

	public Charset getCharSet() {
		return charSet;
	}

	public CabecalhoDbf getCabecalho() {
		return cabecalho;
	}
	
	// Recupera a próxima linha do arquivo de registro
	public List<Campo> proximosRegistros() {
		// Se Por algum motivo houveram caracteres sobrando após o cabeçalho, pula eles. Em geral vai pular apenas o caracter de "presente ou deletado"
		try {
			byte temp_b;
			do {
				temp_b = dbfStream.readByte();
				// Pode ser que ja tenha terminado o arquivo, nesse caso retorna para indicar que não há mais nada para ler
				if(temp_b == CabecalhoDbf.caracterDeTermino) 
					return null;
//				else if(temp_b == CaracterInicial.DADO_DELETADO.getValue()) dbfStream.skip(cabecalho.getTamanhoDoRegistro()-1);
				
			}while(temp_b != CaracterInicial.DADO_PRESENTE.getValue() && temp_b != CaracterInicial.DADO_DELETADO.getValue());
		} catch(EOFException e) {
			return null; // Em caso de leitura posterior ao caracter de termino, retornará null para indicar fim do arquivo
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// A partir daqui é segudo dizer que há uma linha para ser lida e não haverá problema na leitura. Então incrementa o contador de registros lidos
		List<Campo> listaDeRegistros = new ArrayList<Campo>();
		recordsLidos++;
		
		// Realiza a leitura de cada coluna e adiciona a lista de registros
		for(int i =0; i < cabecalho.getTamanhoCampo();i++) {
			listaDeRegistros.add(new Campo(cabecalho.getCampos()[i].getNome(),getValorCampo(cabecalho.getCampos()[i]),cabecalho.getCampos()[i].getTipo()));
		}
		
		return listaDeRegistros;
	}
	
	private byte[] getValorCampo(CampoDbf campoDbf) {
		try {
		int bytesLidos = 0;
		byte byteArray[] = new byte[campoDbf.getTamanhoDoCampo()];
		
			bytesLidos += dbfStream.read(byteArray);
			
			
			return byteArray;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public int getRecordsLidos() {
		return recordsLidos;
	}


	public static String[] getValuesAsString(List<Campo> registros, Charset charset) {
		if(registros == null) return null;
		List<String> l = new ArrayList<>();
		registros.forEach(e -> l.add(e.getValorAsString(charset)));
		
		return l.toArray(new String[l.size()]);
		
	}
	
	public String[] getValuesAsString(List<Campo> registros) {
		return LeitorDbf.getValuesAsString(registros, this.charSet);
		
	}

}
