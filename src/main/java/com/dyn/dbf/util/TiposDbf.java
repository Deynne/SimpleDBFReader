package com.dyn.dbf.util;

public enum TiposDbf {
	
	CARACTER ('C'), DATA('D'), FLUTUANTE('F'),LOGICO('L'),MEMO('M'),NUMERICO('N');
	
	private byte letra;
	
	private TiposDbf(char letra) {
		this.letra = (byte)letra;
	}
	
	public static TiposDbf encontrarPorValor(byte letra) {
		for(TiposDbf t : TiposDbf.values()) {
			if(t.letra == letra) {
				return t;
			}
		}
		return null;
	}
	
	public char getLetra() {
		return (char)letra;
	}
	
	
}
