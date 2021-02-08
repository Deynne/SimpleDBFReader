package br.com.pbprev.model.dbf;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import br.com.pbprev.exceptions.CampoDbfIlegalException;

public class CampoDbf {
	
	private Charset charSet;
	
	// A explicação para estes campos do cabeçalho podem ser encontradas no seguinte link: https://en.wikipedia.org/wiki/.dbf
	
	private byte nome[] = new byte[11]; // byte 0-10
	private byte tipo; // byte 11
	private byte reservado1[] = new byte[4]; // byte 12-15
	// Tem um máximo de 254
	private byte tamanhoDoCampo; // byte 16
	private byte contagemDecimal; // byte 17
	private byte idAreaDeTrabalho[] = new byte[2]; // byte 18-19
	private byte exemplo; // byte 20
	private byte reservado2[] = new byte[10]; // byte 21-30
	private byte flagMdx; // byte 31
	


	private CampoDbf() {
	}

	public String getNome() {
		int fimString = nome.length;
		for(int i = 0; i < nome.length;i++) {
			if(nome[i] == (byte)'\0') {
				fimString = i;
			}
		}
		return new String(nome,0,fimString,charSet);
	}
	
	public static CampoDbf buildCampo(DataInputStream dbfStream, Charset charSet) {
		try {
			
			byte byteTemporario = dbfStream.readByte();
			// Se o caracter sendo lido é o caracter de termino, chegamos ao fim do arquivo.
			if(byteTemporario == (byte) CabecalhoDbf.caracterDeTermino) return null;
			CampoDbf campo = new CampoDbf();
			campo.setCharSet(charSet);
			
			// Ja lemos o primeiro caracter do nome para verificar se não era fim do arquivo.
			// Tendo verificado que não era readicionamos este caracter ao campo de nome
			byte[] tempByteArray = campo.getNomeAsByte();

			dbfStream.readFully(tempByteArray, 1, 10);
			tempByteArray[0] = byteTemporario;

			campo.setTipo(dbfStream.readByte());
			
			tempByteArray = campo.getReservado1();
			dbfStream.readFully(tempByteArray, 0, 4);
			
			campo.setTamanhoDoCampo(dbfStream.readByte());
			
			campo.setContagemDecimal(dbfStream.readByte());
			
			tempByteArray = campo.getIdAreaDeTrabalho();
			dbfStream.readFully(tempByteArray,0,2);
			
			campo.setExemplo(dbfStream.readByte());
			
			tempByteArray = campo.getReservado2();
			dbfStream.readFully(tempByteArray,0,10);
			
			campo.setFlagMdx(dbfStream.readByte());
			
			return campo;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	public byte[] getNomeAsByte() {
		return nome;
	}

	public void setNome(byte[] nome) throws CampoDbfIlegalException {
		if(nome.length < 11) throw new CampoDbfIlegalException("O campo nome contém a quantidade incorreta de bytes");
		this.nome = nome;
	}

	public byte getTipo() {
		return tipo;
	}

	public void setTipo(byte tipo) {
		this.tipo = tipo;
	}

	public byte[] getReservado1() {
		return reservado1;
	}

	public void setReservado1(byte[] reservado1) throws CampoDbfIlegalException {
		if(reservado1.length < 4) throw new CampoDbfIlegalException("O campo reservado1 contém a quantidade incorreta de bytes");
		this.reservado1 = reservado1;
	}

	public byte getTamanhoDoCampo() {
		return tamanhoDoCampo;
	}

	public void setTamanhoDoCampo(byte tamanhoDoCampo) {
		this.tamanhoDoCampo = tamanhoDoCampo;
	}

	public byte getContagemDecimal() {
		return contagemDecimal;
	}

	public void setContagemDecimal(byte contagemDecimal) {
		this.contagemDecimal = contagemDecimal;
	}

	public byte[] getIdAreaDeTrabalho() {
		return idAreaDeTrabalho;
	}

	public void setIdAreaDeTrabalho(byte[] idAreaDeTrabalho) throws CampoDbfIlegalException {
		if(idAreaDeTrabalho.length < 2) throw new CampoDbfIlegalException("O campo idAreaDeTrabalho contém a quantidade incorreta de bytes");
		this.idAreaDeTrabalho = idAreaDeTrabalho;
	}

	public byte getExemplo() {
		return exemplo;
	}

	public void setExemplo(byte exemplo) {
		this.exemplo = exemplo;
	}

	public byte[] getReservado2() {
		return reservado2;
	}

	public void setReservado2(byte[] reservado2) throws CampoDbfIlegalException {
		if(reservado2.length < 10) throw new CampoDbfIlegalException("O campo reservado2 contém a quantidade incorreta de bytes");
		this.reservado2 = reservado2;
	}

	public byte getFlagMdx() {
		return flagMdx;
	}

	public void setFlagMdx(byte flagMdx) {
		this.flagMdx = flagMdx;
	}
	
	public Charset getCharSet() {
		return charSet;
	}

	public void setCharSet(Charset charSet) {
		this.charSet = charSet;
	}

	@Override
	public String toString() {
		return "CampoDbf [ nome=" + getNome() + ", tipo=" + tipo + ", reservado1="
				+ Arrays.toString(reservado1) + ", tamanhoDoCampo=" + tamanhoDoCampo + ", contagemDecimal="
				+ contagemDecimal + ", idAreaDeTrabalho=" + Arrays.toString(idAreaDeTrabalho) + ", exemplo=" + exemplo
				+ ", reservado2=" + Arrays.toString(reservado2) + ", flagMdx=" + flagMdx + ", charSet=" + charSet + " ]\n";
	}

	
}
