package com.dyn.dbf.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import com.dyn.dbf.util.TiposDbf;

/**
 * Um campo do cabeçalho do dbf. Cada campo representa uma coluna do arquivo.
 * @author Deynne Silva
 * @version 1.0
 */
public class CampoDbf {
	
	private Charset charset;
	
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
	

	/**
	 * Construtor basico do campo.
	 */
	private CampoDbf() { }

	/**
	 * O nome do campo como Texto
	 * @return Uma {@link String} do nome do campo.
	 */
	public String getNome() {
		int fimString = nome.length;
		for(int i = 0; i < nome.length;i++) {
			if(nome[i] == (byte)'\0') {
				fimString = i;
			}
		}
		return new String(nome,0,fimString,charset);
	}
	/**
	 * O Construtor do campo. Realiza a leitura do stream de dados do dbf e monta o campo.
	 * O padrão de montagem do campo é feito seguindo o formato definido para o arquivo dbf.
	 * @param dbfStream
	 * @param charset
	 * @return
	 */
	public static CampoDbf buildCampo(DataInputStream dbfStream, Charset charset) {
		try {
			
			byte byteTemporario = dbfStream.readByte();
			// Se o caracter sendo lido é o caracter de termino, chegamos ao fim do arquivo.
			if(byteTemporario == (byte) CabecalhoDbf.caracterDeTermino) return null;
			CampoDbf campo = new CampoDbf();
			campo.setCharSet(charset);
			
			// Ja lemos o primeiro caracter do nome para verificar se não era fim do arquivo.
			// Tendo verificado que não era readicionamos este caracter ao campo de nome
			// O get retorna o array que conterá o nome (já instanciado)
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
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	/**
	 * Para quem deseja trabalhar com o nome no formato original
//	 * @return um <b>byte[ ]</b> representando o nome do campo.
	 */
	public byte[] getNomeAsByte() {
		return nome;
	}

	/**
	 * O tipo do dado presente no campo
	 * @return Um {@link TiposDbf} representando o tipo presente no campo.
	 */
	public TiposDbf getTipo() {
		return TiposDbf.encontrarPorValor(tipo);
	}

	private void setTipo(byte tipo) {
		this.tipo = tipo;
	}

	public byte[] getReservado1() {
		return reservado1;
	}

	/**
	 * Quantidade de bytes do campo
	 * @return Um <b>byte</b> representando a quantidade de byte do campo.
	 */
	public byte getTamanhoDoCampo() {
		return tamanhoDoCampo;
	}

	
	private void setTamanhoDoCampo(byte tamanhoDoCampo) {
		this.tamanhoDoCampo = tamanhoDoCampo;
	}

	public byte getContagemDecimal() {
		return contagemDecimal;
	}

	private void setContagemDecimal(byte contagemDecimal) {
		this.contagemDecimal = contagemDecimal;
	}

	public byte[] getIdAreaDeTrabalho() {
		return idAreaDeTrabalho;
	}

	public byte getExemplo() {
		return exemplo;
	}

	private void setExemplo(byte exemplo) {
		this.exemplo = exemplo;
	}

	public byte[] getReservado2() {
		return reservado2;
	}

	public byte getFlagMdx() {
		return flagMdx;
	}

	private void setFlagMdx(byte flagMdx) {
		this.flagMdx = flagMdx;
	}
	
	/**
	 * O charset sendo utilizado para gerar o nome.
	 * @return O {@link Charset} que esta está sendo utilizado para criar a string do nome.
	 */
	public Charset getCharSet() {
		return charset;
	}

	private void setCharSet(Charset charset) {
		this.charset = charset;
	}

	@Override
	public String toString() {
		return "CampoDbf [ nome=" + getNome() + ", tipo=" + tipo + ", reservado1="
				+ Arrays.toString(reservado1) + ", tamanhoDoCampo=" + tamanhoDoCampo + ", contagemDecimal="
				+ contagemDecimal + ", idAreaDeTrabalho=" + Arrays.toString(idAreaDeTrabalho) + ", exemplo=" + exemplo
				+ ", reservado2=" + Arrays.toString(reservado2) + ", flagMdx=" + flagMdx + ", charset=" + charset + " ]\n";
	}

	
}
