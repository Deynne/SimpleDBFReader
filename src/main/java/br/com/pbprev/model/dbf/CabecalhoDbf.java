package br.com.pbprev.model.dbf;

import java.util.List;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class CabecalhoDbf {

	private Charset charSet;
	
	// A explicação para estes campos do cabeçalho podem ser encontradas no seguinte link: https://en.wikipedia.org/wiki/.dbf
	private byte tipoDeDbf; // byte 0
	private byte ano; // byte 1
	private byte mes; // byte 2
	private byte dia; // byte 3
	// TODO ver o que isso significa
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
	public static final byte caracterDeTermino = 0x0D; // caracter de termino do array de campo
	
	public CabecalhoDbf() {
		this(StandardCharsets.UTF_8);
	}
	
	public CabecalhoDbf(Charset charSet) {
		this.charSet = charSet;
	}
	
	public void buildCabecalho(DataInputStream dbfStream) {
		if(dbfStream == null) return;
		
		try {
			tipoDeDbf = dbfStream.readByte();
			ano = dbfStream.readByte();
			mes = dbfStream.readByte();
			dia = dbfStream.readByte();
			numeroDeRegistros = Integer.reverseBytes(dbfStream.readInt());
			tamanhoHeader = Short.reverseBytes(dbfStream.readShort());
			tamanhoDoRegistro = Short.reverseBytes(dbfStream.readShort());
			
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
			instanciaCampos(dbfStream);
			dbfStream.readByte(); // caracter de termino
//			dbfStream.readShort()
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	private void instanciaCampos(DataInputStream dbfStream) {
		List<CampoDbf> listaDeCampos = new ArrayList<CampoDbf>();
		
		final int tamanhoDoCampo = 32;
		int lendo = tamanhoDoCampo; 
		
		while(lendo <= this.tamanhoHeader - tamanhoDoCampo) {
			CampoDbf campo = CampoDbf.buildCampo(dbfStream, charSet);
			if(campo == null) return;
			else listaDeCampos.add(campo);
			lendo += tamanhoDoCampo;
		}
		this.campos = listaDeCampos.toArray(new CampoDbf[listaDeCampos.size()]);
	}

	public byte getTipoDeDbf() {
		return tipoDeDbf;
	}

	public void setTipoDeDbf(byte tipoDeDbf) {
		this.tipoDeDbf = tipoDeDbf;
	}

	public byte getAno() {
		return ano;
	}

	public void setAno(byte ano) {
		this.ano = ano;
	}

	public byte getMes() {
		return mes;
	}

	public void setMes(byte mes) {
		this.mes = mes;
	}

	public byte getDia() {
		return dia;
	}

	public void setDia(byte dia) {
		this.dia = dia;
	}

	public int getNumeroDeRegistros() {
		return numeroDeRegistros;
	}

	public void setNumeroDeRegistros(int numeroDeRegistros) {
		this.numeroDeRegistros = numeroDeRegistros;
	}

	public short getTamanhoHeader() {
		return tamanhoHeader;
	}

	public void setTamanhoHeader(short tamanhoHeader) {
		this.tamanhoHeader = tamanhoHeader;
	}

	public short getTamanhoDoRegistro() {
		return tamanhoDoRegistro;
	}

	public void setTamanhoDoRegistro(short tamanhoDoRegistro) {
		this.tamanhoDoRegistro = tamanhoDoRegistro;
	}

	public byte[] getReservado1() {
		return reservado1;
	}

	public void setReservado1(byte[] reservado1) {
		this.reservado1 = reservado1;
	}

	public byte getFlagDeTransacao() {
		return flagDeTransacao;
	}

	public void setFlagDeTransacao(byte flagDeTransacao) {
		this.flagDeTransacao = flagDeTransacao;
	}

	public byte getFlagEncriptacao() {
		return flagEncriptacao;
	}

	public void setFlagEncriptacao(byte flagEncriptacao) {
		this.flagEncriptacao = flagEncriptacao;
	}

	public byte[] getReservadoDOS() {
		return reservadoDOS;
	}

	public void setReservadoDOS(byte[] reservadoDOS) {
		this.reservadoDOS = reservadoDOS;
	}

	public byte getFlagMdx() {
		return flagMdx;
	}

	public void setFlagMdx(byte flagMdx) {
		this.flagMdx = flagMdx;
	}

	public byte getIdDriverDeLinguagem() {
		return idDriverDeLinguagem;
	}

	public void setIdDriverDeLinguagem(byte idDriverDeLinguagem) {
		this.idDriverDeLinguagem = idDriverDeLinguagem;
	}

	public byte[] getReservado2() {
		return reservado2;
	}

	public void setReservado2(byte[] reservado2) {
		this.reservado2 = reservado2;
	}

	public CampoDbf[] getCampos() {
		return campos;
	}

	public void setCampos(CampoDbf[] campos) {
		this.campos = campos;
	}

	public byte getCaracterDeTermino() {
		return caracterDeTermino;
	}
	
	public int getTamanhoCampo() {
		return this.campos.length;
	}

	@Override
	public String toString() {
		return "CabecalhoDbf [ tipoDeDbf=" + tipoDeDbf + ", ano=" + ano + ", mes=" + mes
				+ ", dia=" + dia + ", numeroDeRegistros=" + numeroDeRegistros + ", tamanhoHeader=" + tamanhoHeader
				+ ", tamanhoDoRegistro=" + tamanhoDoRegistro + ", reservado1=" + Arrays.toString(reservado1)
				+ ", flagDeTransacao=" + flagDeTransacao + ", flagEncriptacao=" + flagEncriptacao + ", reservadoDOS="
				+ Arrays.toString(reservadoDOS) + ", flagMdx=" + flagMdx + ", idDriverDeLinguagem="
				+ idDriverDeLinguagem + ", reservado2=" + Arrays.toString(reservado2) + ", campos="
				+ Arrays.toString(campos) + ", charSet=" + charSet + " ]\n";
	}

	
	
	
}
