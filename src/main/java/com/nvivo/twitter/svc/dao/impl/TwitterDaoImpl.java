package com.nvivo.twitter.svc.dao.impl;

import com.nvivo.twitter.svc.dao.TwitterDao;
import com.nvivo.twitter.svc.model.MediaDownloadJob;
import com.nvivo.twitter.svc.model.TweetPlaylistResource;
import com.nvivo.twitter.svc.model.TweetResource;
import org.apache.log4j.Logger;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiegoT on 13/05/2016.
 */
public class TwitterDaoImpl implements TwitterDao {
    private DBConnection dbConnection;

    final static Logger LOGGER = Logger.getLogger(TwitterDaoImpl.class);

    @Override
    public TweetPlaylistResource addPlaylist(List<TweetResource> tweets) {
        TweetPlaylistResource playlist = new TweetPlaylistResource();

        String insertQuery = String.format("INSERT INTO playlist (dateTime, title, description) " +
                "VALUES ('%s', '%s', '%s')", LocalDateTime.now(), "lista de tweets", "-----");
        final long playlistId;

        try {
            dbConnection.openConnection();

            tweets.stream().forEach(t -> {
                final String query = String.format("INSERT INTO tweets (id, hashtag, username, fullname, message, urlAvatar, localUrlAvatar, dateTime, isRetweet) " +
                        "VALUES (%s, '%s', '%s', '%s', '%s', '%s', '', '%s', %s)", t.getId().toString(), t.getHashtag(), t.getUserName(), t.getFullName(), t.getMessage(), t.getUrlAvatar(), t.getDateTime(), String.valueOf(t.isRetweet()));
                dbConnection.executeCommand(query);
            });

            dbConnection.executeCommand(insertQuery);
            ResultSet result = dbConnection.getQuery("SELECT IDENTITY() AS LASTID");
            result.first();
            playlistId = result.getLong("LASTID");

            tweets.stream().forEach(t -> {
                final String query = String.format("INSERT INTO playlistDetail (playlistId, tweetId) VALUES (%s, %s)", playlistId, t.getId());
                dbConnection.executeCommand(query);
            });

            playlist = getPlaylist(playlistId);

        } catch (Exception sqle) {
            LOGGER.error("No se pudo guardar el playlist");
            LOGGER.error(sqle);
        }

        return playlist;
    }

    @Override
    public TweetPlaylistResource getPlaylist(long id) {
        List<TweetPlaylistResource> list = new ArrayList<>();

        String query = "SELECT id, title, datetime, description FROM playlist WHERE id = " + id;

        try {
            dbConnection.openConnection();
            ResultSet result = dbConnection.getQuery(query);

            while (result.next()) {
                TweetPlaylistResource playlist = new TweetPlaylistResource();

                playlist.setId(result.getLong("id"));
                playlist.setDateTime(result.getTimestamp("datetime").toLocalDateTime().toString());
                playlist.setTitle(result.getString("title"));
                playlist.setDescription(result.getString("description"));
                playlist.setTweets(getPlaylistDetail(playlist.getId()));

                list.add(playlist);
            }
            result.close();

        } catch (SQLException sqlE) {
            LOGGER.error("No se pudo cargar el playlist " + id);
            LOGGER.error(sqlE);
        }

        return list.get(0);
    }

    @Override
    public void removePlaylist(long playlistId) {
        try {
            dbConnection.openConnection();

            String deleteQuery = String.format("DELETE playlist WHERE id = %s", playlistId);
            dbConnection.executeCommand(deleteQuery);
        } catch (Exception e) {
            LOGGER.error("No se pudo eliminar el playlist " + playlistId);
        }
    }



    @Override
    public List<TweetPlaylistResource> getPlaylists() {
        List<TweetPlaylistResource> list = new ArrayList<>();

        String query = "SELECT id, title, datetime, description FROM playlist";

        try {
            dbConnection.openConnection();
            ResultSet result = dbConnection.getQuery(query);

            while (result.next()) {
                TweetPlaylistResource playlist = new TweetPlaylistResource();

                playlist.setId(result.getLong("id"));
                playlist.setDateTime(result.getTimestamp("datetime").toLocalDateTime().toString());
                playlist.setTitle(result.getString("title"));
                playlist.setDescription(result.getString("description"));
                playlist.setTweets(getPlaylistDetail(playlist.getId()));

                list.add(playlist);
            }
            result.close();

        } catch (SQLException sqlE) {
            LOGGER.error("No se pudieron cargar los playlist de la BD.");
        }

        return list;
    }

    @Override
    public void updateUrlMedia(List<MediaDownloadJob> mediaDownloadJobs) {

        try {
            dbConnection.openConnection();

            String updateQuery = "UPDATE tweets SET localUrlAvatar = '%s' WHERE id = %s";

            mediaDownloadJobs.stream().forEach(mediaDownloadJob -> {
                dbConnection.executeCommand(String.format(updateQuery, mediaDownloadJob.getDestinationMediaUrl(), mediaDownloadJob.getMediaId()));
            });

        } catch (Exception e) {
            LOGGER.error("No se pudo actualizar las url locales de los avatars.");
        }
    }

    private List<TweetResource> getPlaylistDetail(long playlistId) {
        List<TweetResource> tweets = new ArrayList<>();

        String query = "SELECT id, hashtag, username, fullname, message, urlAvatar, localUrlAvatar, dateTime, isRetweet ";
        query += "FROM tweets, playlistDetail WHERE playlistDetail.tweetId = tweets.id ";
        query += "AND playlistDetail.playlistId = " + playlistId;

        try {
            dbConnection.openConnection();
            ResultSet result = dbConnection.getQuery(query);

            while (result.next()) {
                TweetResource tw = new TweetResource();

                tw.setId(((Long)result.getLong("id")).toString());
                tw.setHashtag(result.getString("hashtag"));
                tw.setUserName(result.getString("username"));
                tw.setFullName(result.getString("fullname"));
                tw.setMessage(result.getString("message"));
                tw.setUrlAvatar(result.getString("urlAvatar"));
                tw.setLocalUrlAvatar(result.getString("localUrlAvatar"));
                tw.setDateTime(result.getTimestamp("datetime").toLocalDateTime().toString());
                tw.setRetweet(result.getBoolean("isRetweet"));

                tweets.add(tw);
            }
            result.close();

        } catch (SQLException sqlE) {
            LOGGER.error("No se pudo cargar el detalle del playlist " + playlistId);
        }

        return tweets;
    }

    private final static int ESTADO_CUPON_PAGO = 3;

    @Override
    public String getDBStatus() {
//        ResultSet rs = dbConnection.getQuery("select [SisAveit].[dbo].[T_Socios].Apellido, [SisAveit].[dbo].[T_Socios].Nombre from [SisAveit].[dbo].[T_Socios] where [SisAveit].[dbo].[T_Socios].NroSocio = '8729'");
        String result = "";
        try{

            dbConnection.openConnection();
            DatabaseMetaData meta = dbConnection.getMetaData();
            result = "Connected with " +
                    meta.getDriverName() + " " + meta.getDriverVersion()
                    + "{ " + meta.getDriverMajorVersion() + "," +
                    meta.getDriverMinorVersion() + " }" + " to " +
                    meta.getDatabaseProductName() + " " +
                    meta.getDatabaseProductVersion() + "\n";

            LOGGER.debug(result);
//            while (rs.next())
//            {
//                result = rs.getString("Apellido") + ", " + rs.getString("Nombre");
//            }
//            rs.close();

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            result = "No database connection!!";
            LOGGER.error(result);
        }
        finally
        {
//            dbConnection.closeConnection();
        }
        return result;
    }


/*
    public List<Vendedor> getGanadoresNCifras(int anio, int cuponPago, int cifras, int numero) {
        List<Vendedor> ganadores = new ArrayList<Vendedor>();

        String cupon = String.valueOf(cuponPago);
        String year = String.valueOf(anio);
        String query = "";
        String numeroString = String.valueOf(numero);
        String numeroCifras = numeroString.substring((numeroString.length()-cifras));
        String numeroExcluir = numeroString.substring((numeroString.length()-cifras-1));
        Vendedor vendedor;

        query += "SELECT s.NroSocio, s.Nombre, s.Apellido,s.NroGrupo, m2.NroCabecera, m2.Nro2 ";
        query += "FROM [SisAveit].[dbo].[T_" + year + "] t2014 ";
        query += "inner join [SisAveit].[dbo].[t_rifa] r on t2014.NroCabecera = r.NroCabecera ";
        query += "inner join [SisAveit].[dbo].T_monton m2 on r.NroCabecera=m2.NroCabecera ";
        query += "inner join [SisAveit].[dbo].T_Socios s on r.NroPersona = s.NroSocio ";
        query += "WHERE YEAR(r.año)='" + year + "' ";
        query += "and t2014.Cupon" + cupon + " = " + ESTADO_CUPON_PAGO + " ";
        query += "and (m2.NroCabecera like '%" + numeroCifras + "' or m2.Nro2 like '%" + numeroCifras + "') ";
        query += "and  not exists (";
        query += "select 1 from [SisAveit].[dbo].[T_" + year + "] t2014 inner join [SisAveit].[dbo].[t_rifa] r on t2014.NroCabecera = r.NroCabecera ";
        query += "inner join [SisAveit].[dbo].T_monton m on r.NroCabecera=m.NroCabecera ";
        query += "inner join [SisAveit].[dbo].T_Socios s on r.NroPersona = s.NroSocio ";
        query += "where YEAR(r.año)='" + year + "' ";
        query += "and t2014.Cupon" + cupon + " = " + ESTADO_CUPON_PAGO + " ";
        query += "and (m.NroCabecera like '%" + numeroExcluir + "' or m.Nro2 like '%" + numeroExcluir + "') ";
        query += "and m.NroCabecera = m2.NroCabecera ";
        query += "and m.Nro2 = m2.Nro2)";

        dbConnection.openConnection();
        ResultSet rs = dbConnection.getQuery(query);

        try{

            while (rs.next())
            {
                vendedor = new Vendedor();
                List<Integer> numerosBoleta = new ArrayList<>();

                vendedor.setNroSocio(rs.getInt("NroSocio"));
                vendedor.setNombre(rs.getString("Nombre").trim());
                vendedor.setApellido(rs.getString("Apellido").trim());
                vendedor.setGrupo(rs.getInt("NroGrupo"));
//                vendedor.setTelefono(rs.getString("Telefono") == null ? rs.getString("Telefono").trim() : "");


                numerosBoleta.add(rs.getInt("NroCabecera"));
                numerosBoleta.add(rs.getInt("Nro2"));

                vendedor.setNumeros(numerosBoleta);

                ganadores.add(vendedor);
            }
            rs.close();

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            dbConnection.closeConnection();
        }
        return ganadores;
    }

    public List<Vendedor> getGanadoresSorteo(int anio, int cuponPago, List<String> numeros) {
        List<Vendedor> ganadores = new ArrayList<Vendedor>();

        //String numerosCVS = "43148,40445,27295,45132,42004,27088,21035,24350,28510,26831,16540,39480,10140,8945,15207,7235,1737,49231,4509,47484";

        String numerosCVS = "";

        for (int i = 0; i < numeros.size() ; i++) {
            numerosCVS += numeros.get(i) +  (i == numeros.size()-1 ? "" : ",");
        }

        String cupon = String.valueOf(cuponPago);
        String year = String.valueOf(anio);
        String query = "";
        Vendedor vendedor;

        query += "SELECT s.NroSocio,s.Nombre,s.Apellido,s.NroGrupo, m.NroCabecera,m.Nro2,";
        query += "(SELECT TOP 1 T_TelefonoSocio.Telefono FROM [SisAveit].[dbo].T_TelefonoSocio,[SisAveit].[dbo].T_Domicilio ";
        query += "WHERE T_TelefonoSocio.nrorefdom = T_Domicilio.NroRefDom ";
        query += "and T_Domicilio.nropersona=s.nroSocio) AS Telefono ";
        query += "FROM [SisAveit].[dbo].[T_" + year + "] t2014 ";
        query += "inner join [SisAveit].[dbo].[t_rifa] r on t2014.NroCabecera = r.NroCabecera ";
        query += "inner join [SisAveit].[dbo].T_monton m on r.NroCabecera=m.NroCabecera ";
        query += "inner join [SisAveit].[dbo].T_Socios s on r.NroPersona = s.NroSocio ";
        query += "WHERE YEAR(r.año)='" + year + "' ";
        query += "and t2014.Cupon" + cupon + " = " + ESTADO_CUPON_PAGO + " ";
        query += "and (m.NroCabecera IN (" + numerosCVS + ") OR m.Nro2 IN (" + numerosCVS + ")) ";
        query += "ORDER BY r.NroCabecera ";

        dbConnection.openConnection();
        ResultSet rs = dbConnection.getQuery(query);

        try{

            while (rs.next())
            {
                vendedor = new Vendedor();
                List<Integer> numerosBoleta = new ArrayList<>();

                vendedor.setNroSocio(rs.getInt("NroSocio"));
                vendedor.setNombre(rs.getString("Nombre").trim());
                vendedor.setApellido(rs.getString("Apellido").trim());
                vendedor.setGrupo(rs.getInt("NroGrupo"));
                vendedor.setTelefono(rs.getString("Telefono").trim());

                numerosBoleta.add(rs.getInt("NroCabecera"));
                numerosBoleta.add(rs.getInt("Nro2"));

                vendedor.setNumeros(numerosBoleta);

                ganadores.add(vendedor);
            }
            rs.close();

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            dbConnection.closeConnection();
        }
        return ganadores;
    }
*/

    public void setDbConnection(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
}
