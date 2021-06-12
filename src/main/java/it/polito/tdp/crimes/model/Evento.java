package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>
{
	public enum eventType {CHIAMATA, GESTITO}; 
	
	private LocalDateTime ora; 
	private Vertice distretto;
	private String category; 
	private eventType tipoEvento;
	 
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

	public eventType getTipoEvento()
	{
		return tipoEvento;
	}

	public Evento(LocalDateTime ora, Vertice distretto, String category,
			it.polito.tdp.crimes.model.Evento.eventType tipoEvento)
	{
		this.ora = ora;
		this.distretto = distretto;
		this.category = category;
		this.tipoEvento = tipoEvento;
	}

	@Override public String toString()
	{
		if(this.tipoEvento == eventType.CHIAMATA)
				return tipoEvento +  " " + ora + " nel distretto: " + distretto + ", num: " + distretto.getNumPoliziotti() + "; cat: " + category;
		else return tipoEvento +  " " + ora + " nel distretto: " + distretto + ", num: " + (distretto.getNumPoliziotti()+1) + "; cat: " + category;
	}

	@Override public int compareTo(Evento other)
	{
		return this.ora.compareTo(other.ora);
	}
	
}
