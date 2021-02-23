package com.dyn.dbf.model;

import java.nio.charset.Charset;

import com.dyn.dbf.util.TiposDbf;

public class Campo {
	private String nome;
	private byte [] valor;
	private TiposDbf tipo;
	
	
	
	public Campo(String nome, byte[] valor, TiposDbf tipo) {
		super();
		this.nome = nome;
		this.valor = valor;
		this.tipo = tipo;
	}
	
	public String getNome() {
		return nome;
	}
	public byte[] getValor() {
		return valor;
	}
	public TiposDbf getTipo() {
		return tipo;
	}
	
	
	public String getValorAsString(Charset charset) {
		return new String(valor,charset);
	}
}
