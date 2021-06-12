package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>
{
	public enum tipoEvento {CHIAMATA, GESTITO}; 
	
	private LocalDateTime ora; 
	private Vertice distretto;
	private String category; 
	private tipoEvento tipoEvento;
	 
	public LocalDateTime getOra()
	{
		return ora;
	}

	public Vertice getDistretto()
	{
		return distretto;
	}

	public String getCategory()
	{
		return category;
	}

	public tipoEvento getTipoEvento()
	{
		return tipoEvento;
	}

	public Evento(LocalDateTime ora, Vertice distretto, String category,
			it.polito.tdp.crimes.model.Evento.tipoEvento tipoEvento)
	{
		this.ora = ora;
		this.distretto = distretto;
		this.category = category;
		this.tipoEvento = tipoEvento;
	}

	@Override public String toString()
	{
		return tipoEvento +  " " + ora + " nel distretto: " + distretto + ", num: " + distretto.getNumPoliziotti() + "; cat: " + category;
	}

	@Override public int compareTo(Evento other)
	{
		return this.ora.compareTo(other.ora);
	}
	
}
