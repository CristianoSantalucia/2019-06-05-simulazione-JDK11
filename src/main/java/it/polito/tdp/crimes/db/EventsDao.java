package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.Vertice;

public class EventsDao
{
	public List<Event> listAllEvents()
	{
		String sql = "SELECT * FROM events";
		try
		{
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Event> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					list.add(new Event(res.getLong("incident_id"), res.getInt("offense_code"),
							res.getInt("offense_code_extension"), res.getString("offense_type_id"),
							res.getString("offense_category_id"), res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"), res.getDouble("geo_lon"), res.getDouble("geo_lat"),
							res.getInt("district_id"), res.getInt("precinct_id"), res.getString("neighborhood_id"),
							res.getInt("is_crime"), res.getInt("is_traffic")));
				}
				catch (Throwable t)
				{
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}

			conn.close();
			return list;

		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Integer> getYears()
	{
		String sql = "SELECT DISTINCT YEAR(e.reported_date) AS year "
					+ "FROM `events` as e "
					+ "ORDER BY year " ; 
		
		List<Integer> list = new ArrayList<>();
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					list.add(res.getInt("year"));
				}
				catch (Throwable t)
				{
					t.printStackTrace();
					System.out.println(res.getInt("year"));
				}
			}
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public Collection<Integer> getDays(Integer year, Integer month)
	{
		String sql = "SELECT DISTINCT DAY(e.reported_date) AS day "
				+ "FROM `events` as e "
				+ "WHERE YEAR(e.reported_date) = ? AND MONTH(e.reported_date) = ? "
				+ "ORDER BY day " ; 
		
		List<Integer> list = new ArrayList<>();
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			st.setInt(2, month);
			ResultSet res = st.executeQuery();
			
			while (res.next())
			{
				try
				{
					list.add(res.getInt("day"));
				}
				catch (Throwable t)
				{
					t.printStackTrace();
					System.out.println(res.getInt("day"));
				}
			}
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void getVertici(List<Vertice> vertici, Integer year)
	{
		String sql = "SELECT e.district_id distretto, AVG(e.geo_lat) lat, AVG(e.geo_lon) lon "
					+ "FROM `events` e "
					+ "WHERE YEAR(e.reported_date) = ? "
					+ "GROUP BY distretto ";  
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql); 
			st.setInt(1, year);
			ResultSet res = st.executeQuery();
			
			while (res.next())
			{
				try
				{
					Vertice v = new Vertice(res.getInt("distretto"), res.getDouble("lat"),  res.getDouble("lon")); 
					if(!vertici.contains(v))
						vertici.add(v);
				}	
				catch (Throwable t)
				{
					t.printStackTrace();
					System.out.println(res.getInt("distretto"));
				}
			}
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	} 
	
	public Integer getCentrale(Integer year, Integer month, Integer day)
	{
		String sql = "SELECT e.district_id AS id, COUNT(*) AS qntCrimi "
					+ "FROM `events` e "
					+ "WHERE YEAR(e.reported_date) = ? "
					+ "AND MONTH(e.reported_date) = ? "
					+ "AND DAY(e.reported_date) = ? "
					+ "GROUP BY e.district_id "
					+ "ORDER BY qntCrimi";  
		Integer d = null; 
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql); 
			st.setInt(1, year);
			st.setInt(2, month);
			st.setInt(3, day);
			ResultSet res = st.executeQuery();
			
			if(res.next())
			{
				 d = res.getInt("id"); 
			}
			conn.close();
			return d; 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null; 
		}
	} 
	
	public List<Event> listEvents(Integer year, Integer month, Integer day)
	{
		String sql = "SELECT * "
					+ "FROM `events` e "
					+ "WHERE YEAR(e.reported_date) = ? "
					+ "		AND MONTH(e.reported_date) = ? "
					+ "		AND DAY(e.reported_date) = ? ";
		List<Event> list = new ArrayList<>();
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			st.setInt(2, month);
			st.setInt(3, day);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					list.add(new Event(res.getLong("incident_id"), res.getInt("offense_code"),
							res.getInt("offense_code_extension"), res.getString("offense_type_id"),
							res.getString("offense_category_id"), res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"), res.getDouble("geo_lon"), res.getDouble("geo_lat"),
							res.getInt("district_id"), res.getInt("precinct_id"), res.getString("neighborhood_id"),
							res.getInt("is_crime"), res.getInt("is_traffic")));
				}
				catch (Throwable t)
				{
					t.printStackTrace();
					System.out.println(res.getInt("incident_id"));
				}
			}
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
