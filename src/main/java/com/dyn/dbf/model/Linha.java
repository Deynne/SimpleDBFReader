package com.dyn.dbf.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Linha {
	private List<Campo> colunas;
	private Charset charset;
	
	public Linha() {
		this(StandardCharsets.UTF_8);
	}
	
	public Linha(Charset charset) {
		this(new ArrayList<Campo>(),charset);
	}
	
	public Linha(List<Campo> colunas, Charset charset) {
		this.colunas = colunas;
		this.charset = charset;
	}
	
	/**
	 * Retorna os dados de uma linha de dados como strings
	 * @param registros A linha da qual se espera obter as informações como string
	 * @param charset O charset que deve ser utilizado na conversão dos dados para string
	 * @return Um {@link String}[] contendo os dados da linha.
	 */
	public String[] getValuesAsString() {
		List<String> l = new ArrayList<>();
		colunas.forEach(e -> l.add(e.getValorAsString(charset)));
		
		return l.toArray(new String[l.size()]);
		
	}
	
	/**
	 * Retorna o valor de um campo da linha, de acordo com o nome, como uma string
	 * @param indice O indice do campo que se deseja obter o valor.
	 * @return Uma {@link String} com o valor do campo. <b>null</b> caso a posição seja invalida.
	 * @see #getValueAsString(String)
	 */
	public String getValueAsString(int indice) {
		Campo c = this.getCampo(indice);
		
		if(c == null) return null;
		
		return c.getValorAsString(charset);
	}
	
	/**
	 * Retorna o valor em seu formato bruto
	 * @param indice O indice do campo que se deseja obter o valor.
	 * @return Um <b>byte</b>[ ] com o valor do campo. <b>null</b> caso a posição seja invalida.
	 * @see #getValue(String)
	 */
	public byte[] getValue(int indice) {
		Campo c = this.getCampo(indice);
		
		if(c == null) return null;
		
		return c.getValor();
	}
	
	/**
	 * Retorna o valor de um campo de acordo com seu tipo
	 * @param nome O indice do campo que se deseja obter o valor.
	 * @return Um {@link Object} de acordo com o tipo do valor do campo. <b>null</b> caso a posição seja invalida.
	 */
	public Object getValueTipado(int indice) {
		Campo c = this.getCampo(indice);
		
		if(c == null) return null;
		
		return c.getValorTipado(charset);
	}
	
	/**
	 * Retorna o valor de um campo da linha, de acordo com o nome, como uma string
	 * @param nome O nome do campo que se deseja obter o valor.
	 * @return Uma {@link String} com o valor do campo. <b>null</b> caso não exista campo com o nome informado.
	 * @see #getValueAsString(int)
	 */
	public String getValueAsString(String nome) {
		Iterator<Campo> i = colunas.iterator();
		Campo c;
		while(i.hasNext()) {
			c = i.next();
			if(c.getNome().trim().equals(nome)) return c.getValorAsString(charset);
		}
		return null;
	}
	
	/**
	 * Retorna o valor em seu formato bruto
	 * @param nome O nome do campo que se deseja obter o valor
	 * @return Um <b>byte</b>[ ] com o valor do campo. <b>null</b> caso não exista campo com o nome informado.
	 * @see #getValue(int)
	 */
	public byte[] getValue(String nome) {
		Iterator<Campo> i = colunas.iterator();
		Campo c;
		while(i.hasNext()) {
			c = i.next();
			if(c.getNome().trim().equals(nome)) return c.getValor();
		}
		return null;
	}
	
	/**
	 * Retorna o valor de um campo de acordo com seu tipo
	 * @param nome O nome do campo que se deseja obter o valor
	 * @return Uma {@link String} com o valor do campo. <b>null</b> caso não exista campo com o nome informado.
	 * @see #getValueTipado(int)
	 */
	public Object getValueTipado(String nome) {
		Iterator<Campo> i = colunas.iterator();
		Campo c;
		while(i.hasNext()) {
			c = i.next();
			if(c.getNome().trim().equals(nome)) return c.getValorTipado();
		}
		return null;
	}
	
	/**
	 * Retorna um campo segundo o indice especificado.
	 * @param indice O valor do indice do campo que se deseja obter.
	 * @return Um {@link Campo} correspondendo ao indice. <b>null</b> caso o indice seja invalido.
	 * @see #getCampo(String)
	 */
	public Campo getCampo(int indice) {
		if(indice < 0 || indice > colunas.size()-1) return null;
		return colunas.get(indice);
	}
	
	/**
	 * Retorna um campo segundo o nomeespecificado.
	 * @param nome O Um {@link String} com o nome do campo
	 * @return Um {@link Campo} correspondendo ao nome do campo informado. <b>null</b> caso o nome do campo seja invalido.
	 * @see Linha#getCampo(int)
	 */
	public Campo getCampo(String nome) {
		Iterator<Campo> i = colunas.iterator();
		Campo c;
		while(i.hasNext()) {
			c = i.next();
			if(c.getNome().trim().equals(nome)) return c;
		}
		return null;
	}

	/**
	 * Retorna a lista de campos da linha
	 * @return Uma {@link List}<{@link Campo}> com os campos presentes na linha.
	 */
	public List<Campo> getColunas() {
		return colunas;
	}

	/**
	 * O {@link Charset} utilizado para criação das {@link String}
	 * @return
	 */
	public Charset getCharset() {
		return charset;
	}

	public void setColunas(List<Campo> colunas) {
		this.colunas = colunas;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}
