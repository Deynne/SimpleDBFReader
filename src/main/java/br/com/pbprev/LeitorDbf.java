package br.com.pbprev;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.pbprev.model.dbf.CabecalhoDbf;
import br.com.pbprev.model.dbf.CampoDbf;
import br.com.pbprev.util.DbfUtils; 

public class LeitorDbf implements Closeable{

	private InputStream inputStream;
	private DataInputStream dbfStream;
	private Charset charSet;
	private CabecalhoDbf cabecalho;
	
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
	
	public String[] proximosRegistrosAsString() {
		List<String> listaDeRegistros = new ArrayList<String>();
		
		try {
			byte temp_b;
			do {
				temp_b = dbfStream.readByte();
				if(temp_b == CabecalhoDbf.caracterDeTermino) 
					return null;
//				else if(temp_b == CaracterInicial.DADO_DELETADO.getValue()) dbfStream.skip(cabecalho.getTamanhoDoRegistro()-1);
				
			}while(temp_b != CaracterInicial.DADO_PRESENTE.getValue() && temp_b != CaracterInicial.DADO_DELETADO.getValue());
		} catch(EOFException e) {
			return null;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i =0; i < cabecalho.getTamanhoCampo();i++) {
			listaDeRegistros.add(new String(getValorCampo(cabecalho.getCampos()[i]),charSet));
//			getValorCampo(cabecalho.getCampos()[i]);
			System.out.print(listaDeRegistros.get(i) + " # ");
//			
//			System.out.print("#");
		}
		
		return listaDeRegistros.toArray(new String[listaDeRegistros.size()]);
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
	
	

}
