package it.polito.tdp.crimes.model;

import com.javadocmd.simplelatlng.LatLng; 

public class Vertice
{
	//simulazione
	private Integer numPoliziotti; 
	
	public void setNumPoliziotti(int n)
	{
		this.numPoliziotti = n;
	}
	public Integer getNumPoliziotti()
	{
		return numPoliziotti;
	}
	public void aumentaPoliziotti()
	{
		this.numPoliziotti++;
	}
	public void diminuisciPoliziotti()
	{
		this.numPoliziotti--;
	}
	
	//default
	private Integer distretto;
	private Double lat; 
	private Double lon;
	private LatLng coordinate;
	
	public Vertice(Integer distretto, Double lat, Double lon)
	{
		this.distretto = distretto;
		this.lat = lat;
		this.lon = lon;
		
		this.coordinate = new LatLng(this.lat, this.lon);
	}
	public LatLng getCoordinate()
	{
		return coordinate;
	}
	public Integer getDistretto()
	{
		return distretto;
	}
	public Double getLat()
	{
		return lat;
	}
	public Double getLon()
	{
		return lon;
	}
	@Override public String toString()
	{
		return "" + distretto ;
	}
	@Override public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((distretto == null) ? 0 : distretto.hashCode());
		return result;
	}
	@Override public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vertice other = (Vertice) obj;
		if (distretto == null)
		{
			if (other.distretto != null) return false;
		}
		else if (!distretto.equals(other.distretto)) return false;
		return true;
	} 
}
