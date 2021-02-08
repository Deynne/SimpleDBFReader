package br.com.pbprev.util;

public enum TiposDbf {
	
	CARACTER ('C'), DATA('D'), FLUTUANTE('F'),LOGICO('L'),MEMO('M'),NUMERICO('N');
	
	private char letra;
	
	private TiposDbf(char letra) {
		this.letra = letra;
	}
	
	
	
}
