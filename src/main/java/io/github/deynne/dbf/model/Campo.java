package io.github.deynne.dbf.model;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.GregorianCalendar;

import io.github.deynne.dbf.util.TiposDbf;

/**
 * Campo de dados do arquivo dbf. Os dados lidos são convertidos para instâncias desta classe.
 * @author Deynne Silva
 * @version 1.0
 */
public class Campo {
	private String nome;
	private byte [] valor;
	private TiposDbf tipo;
	private Charset charset;
	
	/**
	 * Construtor basico do campo.
	 * <p>
	 * 	Para este construto o charset Padrão é {@link StandardCharsets#UTF_8}
	 * </p>
	 * @param nome Uma {@link String} com o nome do campo. Equivale a {@link CampoDbf#getNome()}.
	 * @param valor Um <b>byte</b>[ ] contendo os dados do campo para a linha em questão.
	 * @param tipo Um {@link TiposDbf} correspondendo ao tipo do campo. Equivale a {@link CampoDbf#getTipo()}
	 * @see #Campo(String, byte[], TiposDbf, Charset)
	 */
	public Campo(String nome, byte[] valor, TiposDbf tipo) {
		this(nome,valor,tipo,StandardCharsets.UTF_8);
	}
	
	/**
	 * Construtor com a definição de {@link Charset}
	 * @param nome Uma {@link String} com o nome do campo. Equivale a {@link CampoDbf#getNome()}.
	 * @param valor Um <b>byte</b>[ ] contendo os dados do campo para a linha em questão.
	 * @param tipo Um {@link TiposDbf} correspondendo ao tipo do campo. Equivale a {@link CampoDbf#getTipo()}
	 * @param charset O {@link Charset} a ser utilizado para a construção de strings.
	 * @see #Campo(String, byte[], TiposDbf)
	 */
	public Campo(String nome, byte[] valor, TiposDbf tipo,Charset charset) {
		this.nome = nome;
		this.valor = valor;
		this.tipo = tipo;
		this.charset = charset;
	}
	/**
	 * O nome do campo
	 * @return uma {@link String} com o nome do campo
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * Os dados do campo
	 * @return Um <b>byte</b>[ ] com os dados do campo.
	 */
	public byte[] getValor() {
		return valor;
	}
	/**
	 * O tipo do dado.
	 * @return um {@link TiposDbf} representando o tipo do dado.
	 */
	public TiposDbf getTipo() {
		return tipo;
	}
	
	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	
	/**
	 * O dado do campo em formato de string.
	 * @return Uma {@link String} com os dados do campo em formato de texto.
	 */
	public String getValorAsString() {
		return this.getValorAsString(this.charset);
	}
	
	public String getValorAsString(Charset charset) {
		return new String(valor,charset);
	}
	
	/**
	 * O dado do campo de acordo com seu tipo.
	 * @param charset O {@link Charset} para qual o tipo {@link TiposDbf#MEMO} deve ser convertido caso o valor seja tipo Memo.
	 * @return De acordo com o tipo do dado pode retornar
	 * <p>
	 * 	{@link Integer} caso seja do tipo {@link TiposDbf#NUMERICO}
	 * </p>
	 * <p>
	 * 	{@link Date} caso seja do tipo {@link TiposDbf#DATA}
	 * </p>
	 * <p>
	 * 	{@link Double} caso seja do tipo {@link TiposDbf#FLUTUANTE}
	 * </p>
	 * <p>
	 * 	{@link Boolean} caso seja do tipo {@link TiposDbf#LOGICO}
	 * </p>
	 * <p>
	 * 	{@link String} caso seja do tipo {@link TiposDbf#MEMO}
	 * </p>
	 * <p>
	 * 	{@link Integer} caso seja do tipo {@link TiposDbf#NUMERICO}
	 * </p>
	 * <p>
	 * 	<b>null</b> caso não seja de nenhum tipo definido.
	 * </p>
	 */
	public Object getValorTipado(Charset charset) {
		ByteBuffer buffer = ByteBuffer.wrap(valor);
		switch(tipo) {
		case CARACTER:
			 return new Character((char)buffer.get());
		case DATA:
			int ano = buffer.get(valor,0,4).asIntBuffer().get();
			int mes = buffer.get(valor,4,2).asIntBuffer().get();
			int dia = buffer.get(valor,6,2).asIntBuffer().get();

			// O parâmetro de mês no gregorian calendar começa em 0;
			return new GregorianCalendar(ano,mes-1,dia).getTime();
		case FLUTUANTE:
			return new Double(buffer.getFloat());
		case LOGICO:
			// O dbf padrão pode assumir os valores lógicos yYtT1 para true e nNfF para false.
			// Há também o caso de campo indefinido com o valor de ?
			
			// Foi decidido que qualquer campo não verdadeiro seria falso.
			byte b = buffer.get();
			if(b == 1 || b == 'y' || b == 'Y' || b == 't' || b == 'T')
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		case MEMO:
			return new String(valor,charset).trim();
		case NUMERICO:
			return new Integer(Integer.parseInt(new String(valor,charset).trim()));
			
		default:
			return null;
		}
	}
	
	/**
	 * O dado do campo de acordo com seu tipo.
	 * <p>
	 * 	Utiliza o charset definido para o campo.
	 * </p>
	 * @return De acordo com o tipo do dado pode retornar
	 * <p>
	 * 	{@link Integer} caso seja do tipo {@link TiposDbf#NUMERICO}
	 * </p>
	 * <p>
	 * 	{@link Date} caso seja do tipo {@link TiposDbf#DATA}
	 * </p>
	 * <p>
	 * 	{@link Double} caso seja do tipo {@link TiposDbf#FLUTUANTE}
	 * </p>
	 * <p>
	 * 	{@link Boolean} caso seja do tipo {@link TiposDbf#LOGICO}
	 * </p>
	 * <p>
	 * 	{@link String} caso seja do tipo {@link TiposDbf#MEMO}
	 * </p>
	 * <p>
	 * 	{@link Integer} caso seja do tipo {@link TiposDbf#NUMERICO}
	 * </p>
	 * <p>
	 * 	<b>null</b> caso não seja de nenhum tipo definido.
	 * </p>
	 */
	public Object getValorTipado() {
		return this.getValorTipado(charset);
	}
}
