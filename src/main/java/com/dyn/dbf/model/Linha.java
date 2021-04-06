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
	 * Retorna os dados de uma linha de dados como strings.
	 * @return Um {@link String}[] contendo os dados da linha.
	 * @see #getValuesAsString(boolean)
	 */
	public String[] getValuesAsString() {
		return this.getValuesAsString(false);
	}
	
	/**
	 * Retorna os dados de uma linha de dados como strings
	 * @param trim Indica se deve eliminar espaços em branco do dado
	 * @return Um {@link String}[] contendo os dados da linha.
	 * @see #getValuesAsString()
	 */
	public String[] getValuesAsString(boolean trim) {
		List<String> l = new ArrayList<>();
		colunas.forEach(e -> l.add(trim?e.getValorAsString(charset).trim():e.getValorAsString(charset)));
		
		return l.toArray(new String[l.size()]);
		
	}
	
	/**
	 * Retorna o valor de um campo da linha, de acordo com o nome, como uma string
	 * @param indice O indice do campo que se deseja obter o valor.
	 * @return Uma {@link String} com o valor do campo. <b>null</b> caso a posição seja invalida.
	 * @see #getValueAsString(int, boolean)
	 * @see #getValueAsString(String)
	 * @see #getValueAsString(String, boolean) 
	 */
	public String getValueAsString(int indice) {
		return this.getValueAsString(indice,false);
	}
	
	/**
	 * Retorna o valor de um campo da linha, de acordo com o nome, como uma string
	 * @param indice O indice do campo que se deseja obter o valor.
	 * @param trim Indica se deve eliminar espaços em branco do dado
	 * @return Uma {@link String} com o valor do campo. <b>null</b> caso a posição seja invalida.
	 * @see #getValueAsString(String)
	 */
	public String getValueAsString(int indice, boolean trim) {
		Campo c = this.getCampo(indice);
		
		if(c == null) return null;
		
		return trim?c.getValorAsString(charset).trim():c.getValorAsString(charset);
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
	 * @param indice O indice do campo que se deseja obter o valor.
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
		return this.getValueAsString(nome,false);
	}
	
	/**
	 * Retorna o valor de um campo da linha, de acordo com o nome, como uma string
	 * @param nome O nome do campo que se deseja obter o valor.
	 * @param trim Indica se deve eliminar espaços em branco do dado
	 * @return Uma {@link String} com o valor do campo. <b>null</b> caso não exista campo com o nome informado.
	 * @see #getValueAsString(int)
	 */
	public String getValueAsString(String nome, boolean trim) {
		Campo c = this.getCampo(nome);
		
		if(c == null) return null;
		
		return trim?c.getValorAsString(charset).trim():c.getValorAsString(charset);
	}
	
	/**
	 * Retorna o valor em seu formato bruto
	 * @param nome O nome do campo que se deseja obter o valor
	 * @return Um <b>byte</b>[ ] com o valor do campo. <b>null</b> caso não exista campo com o nome informado.
	 * @see #getValue(int)
	 */
	public byte[] getValue(String nome) {
		Campo c = this.getCampo(nome);
		
		if(c == null) return null;
		
		return c.getValor();
	}
	
	/**
	 * Retorna o valor de um campo de acordo com seu tipo
	 * @param nome O nome do campo que se deseja obter o valor
	 * @return Uma {@link String} com o valor do campo. <b>null</b> caso não exista campo com o nome informado.
	 * @see #getValueTipado(int)
	 */
	public Object getValueTipado(String nome) {
		Campo c = this.getCampo(nome);
		
		if(c == null) return null;
		
		return c.getValorTipado();
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
		for(int i = 0, j = this.colunas.size()- 1; i <= this.colunas.size()/2;i++,j--) {
			if(this.colunas.get(i).getNome().trim().equals(nome)) {
				return this.colunas.get(i);
			}
			else if(this.colunas.get(j).getNome().trim().equals(nome)) {
				return this.colunas.get(j);
			}
		}
		
		return null;
	}

	/**
	 * Retorna a lista de campos da linha
	 * @return Uma {@link List}&lt;{@link Campo}&gt; com os campos presentes na linha.
	 */
	public List<Campo> getColunas() {
		return colunas;
	}

	/**
	 * O {@link Charset} utilizado para criação das {@link String}
	 * @return o {@link Charset} utilizado para tratamento dos dados como strings
	 */
	public Charset getCharset() {
		return charset;
	}
	
	/**
	 * Define as colunas que estão presentes na linha
	 * @param colunas uma {@link List}&lt;{@link Campo}&gt; com as colunas presentes na linha
	 */
	public void setColunas(List<Campo> colunas) {
		this.colunas = colunas;
	}

	/**
	 * Define o {@link Charset} a ser utilizado para criação das strings dos dados
	 * @param charset o padrão de {@link Charset} a ser utilizado
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}
