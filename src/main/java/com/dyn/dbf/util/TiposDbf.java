package com.dyn.dbf.util;

/**
 * Enum com os possíveis tipos de dados do arquivo dbf.
 * @author Deynne Silva
 * @version 1.0
 * @see #CARACTER
 */
public enum TiposDbf {
	/**
	 * corresponte ao byte 
	 */
	CARACTER ('C'), 
	/**
	 * 
	 */
	DATA('D'), 
	/**
	 * 
	 */
	FLUTUANTE('F'),
	/**
	 * 
	 */
	LOGICO('L'),
	/**
	 * 
	 */
	MEMO('M'),
	/**
	 * 
	 */
	NUMERICO('N');
	
	private byte letra;
	
	private TiposDbf(char letra) {
		this.letra = (byte)letra;
	}
	
	/**
	 * 
	 * @param letra
	 * @return
	 */
	public static TiposDbf encontrarPorValor(byte letra) {
		for(TiposDbf t : TiposDbf.values()) {
			if(t.letra == letra) {
				return t;
			}
		}
		return null;
	}
	/**
	 * 
	 * @return
	 */
	public char getLetra() {
		return (char)letra;
	}
	
	
}
