/*
 * classe Model preimpostata 
 * questo documento è soggetto ai relativi diritti di ©Copyright
 * giugno 2021
 */ 

package it.polito.tdp.crimes.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*; 
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.drawing.FRLayoutAlgorithm2D.TemperatureModel;
import org.jgrapht.graph.*;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;
import it.polito.tdp.crimes.model.Evento.tipoEvento;

public class Model
{
	private EventsDao dao;
	private List<Vertice> vertici; 
	private Graph<Vertice, DefaultWeightedEdge> grafo; 

	public Model()
	{
		this.dao = new EventsDao();
	}

	public Collection<Integer> getYears()
	{
		return this.dao.getYears();
	}
	public Collection<Integer> getDays(Integer year, Integer month)
	{
		return this.dao.getDays(year, month);
	}

	public void creaGrafo(Integer year)
	{
		// ripulisco mappa e grafo
		this.vertici = new ArrayList<>(); 
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); 

		/// vertici 
		this.dao.getVertici(this.vertici, year); //riempio la mappa
		Graphs.addAllVertices(this.grafo, this.vertici); 

		/// archi
		for (Vertice v1 : this.vertici)
		{
			for (Vertice v2 : this.vertici)
			{
				double dist = LatLngTool.distance(v1.getCoordinate(), v2.getCoordinate(), LengthUnit.KILOMETER);
				if(!v1.equals(v2))
					Graphs.addEdge(this.grafo, v2, v1, dist); 
			}
		}
	}
	public String stampaGrafo()
	{
		String s = ""; 
		for (Vertice v : this.grafo.vertexSet())
		{
			s += "\n\nVertice " + v + ":"; 
			for (Vertice ad : this.getAdiacenti(v))
				s += String.format("\n Stazione %s, %.2f KM ;", ad,this.grafo.getEdgeWeight(this.grafo.getEdge(v, ad)) ); 
		}
		return s; 
	}
	//lista ordianta
	private List<Vertice> getAdiacenti(Vertice v)
	{
		List<Vertice> adiacenti = new ArrayList<>(Graphs.neighborListOf(this.grafo, v));
		adiacenti.sort((a1,a2)->Double.compare(this.grafo.getEdgeWeight(this.grafo.getEdge(v, a1)),this.grafo.getEdgeWeight(this.grafo.getEdge(v, a2))));
		return adiacenti ; 
	}

	public int getNumVertici()
	{
		return this.grafo.vertexSet().size();
	}
	public int getNumArchi()
	{
		return this.grafo.edgeSet().size();
	}
	public Collection<Vertice> getVertici()
	{
		return this.grafo.vertexSet();
	}
	public Collection<DefaultWeightedEdge> getArchi()
	{
		return this.grafo.edgeSet();
	}

	//SIMULAZIONE 
	PriorityQueue<Evento> eventi; 
	Integer eventiMalGestiti ; 
	Integer eventiNONGestiti ; 
	Integer numPoliziotti; 
	List<Vertice> distretti; 
	final Integer TEMPO_ORDINARIO = 2; 
	final Integer TEMPO_STRAORDINARIO = 1; //50% prob per eventi "all other crimes"
	final Integer VELOCITA = 60; 

	public void simula(Integer year, Integer month, Integer day, Integer numPoliziotti)
	{
		Vertice centrale = this.vertici.get(this.dao.getCentrale(year, month, day)-1); 
		this.eventiMalGestiti = 0; 
		this.eventiNONGestiti = 0; 
		this.numPoliziotti = numPoliziotti;
		this.distretti = new ArrayList<>(this.vertici);
		distretti.sort((d1,d2)->d1.getDistretto().compareTo(d2.getDistretto()));
		if (centrale != null)
		{
			//imposto num Poliziotti
			for (Vertice v : this.distretti)
				v.setNumPoliziotti(0);
		
			centrale.setNumPoliziotti(numPoliziotti);
			
			//creo le chiamate
			this.eventi = new PriorityQueue<>(); 

			ArrayList<Event> events = new ArrayList<>(this.dao.listEvents(year, month, day));
			for(Event event : events)
				this.eventi.add(new Evento(event.getReported_date(), this.distretti.get(event.getDistrict_id()-1), event.getOffense_category_id(), tipoEvento.CHIAMATA));
			
			while(!this.eventi.isEmpty())
			{
				Evento e = this.eventi.poll();
				this.porcessEvent(e); 
			}
			System.out.println("\nTOT MAL GESTITI: "+ this.eventiMalGestiti + " su " + events.size());
			System.out.println("\nTOT NON GESTITI: "+ this.eventiNONGestiti + " su " + events.size());
		}
		else 
			System.out.println("ERRORE");
		
	}

	private void porcessEvent(Evento e)
	{
		System.out.println("\n" + e);

		if(e.getTipoEvento().equals(tipoEvento.CHIAMATA))
		{
			//selezione poliziotto piu vicino 
			Vertice chiamata = e.getDistretto(); 
			Vertice piuVicino = null; 
			//cerco distrtto piu vincino con numPoliziotti > 0 
			if(chiamata.getNumPoliziotti() > 0)
			{
				piuVicino = chiamata; 
				piuVicino.diminuisciPoliziotti();
				System.out.println("#### POLIZIOTTO ALLA RISCOSSA dal distretto: " + chiamata + ", ancora disponibili: " + chiamata.getNumPoliziotti());
			}
			else for(Vertice v : this.getAdiacenti(chiamata))
			{
				if(v.getNumPoliziotti()>0)
				{
					piuVicino = v; 
					piuVicino.diminuisciPoliziotti();
					System.out.println("#### POLIZIOTTO ALLA RISCOSSA dal distretto: " + v + ", ancora disponibili: " + v.getNumPoliziotti());
					break; 
				}
			} 
			if(piuVicino == null)
			{
				System.out.println("*** NESSUN POLIZIOTTO DISPONIBILE"); 
				this.eventiNONGestiti++;
				return; 
			}

			//tempo arrivo 
			LocalDateTime oraArrivo = e.getOra();
			if(!piuVicino.equals(chiamata))
			{
				Double distanza = this.grafo.getEdgeWeight(this.grafo.getEdge(chiamata, piuVicino)); 
				Long tempoPercorrenza = (long) (distanza/this.VELOCITA * 60);
				System.out.println(" -> TEMPO PERCORRENZA " + tempoPercorrenza + " min");
				if(tempoPercorrenza > 15)
				{
					System.out.println("@@@@@@ POLIZIOTTO IN RITARDO");
					this.eventiMalGestiti++;
				}

				oraArrivo = e.getOra().plusMinutes(tempoPercorrenza); 
			} 

			int tempoGestione = this.TEMPO_ORDINARIO; 
			//tempo di gestione
			if(e.getCategory().equals("all-other-crimes"))
			{
				double prob = Math.random(); 
				if(prob<.5)
					tempoGestione = this.TEMPO_STRAORDINARIO; 
			}
			this.eventi.add(new Evento(oraArrivo.plus(Duration.ofHours(tempoGestione)), chiamata, e.getCategory(), tipoEvento.GESTITO));
		}
		else if(e.getTipoEvento().equals(tipoEvento.GESTITO))
		{
			 e.getDistretto().aumentaPoliziotti();
		}
	}
}
