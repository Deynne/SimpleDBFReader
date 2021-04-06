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
	 * @param letra Um <b>byte</b> com o valor da letra que se deve buscar no enum.
	 * @return o {@link TiposDbf} associado a letra informada
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
	 * @return a letra associada ao valor do enum
	 */
	public char getLetra() {
		return (char)letra;
	}
	
	
}
