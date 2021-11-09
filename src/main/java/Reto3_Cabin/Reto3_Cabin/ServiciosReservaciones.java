/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reto3_Cabin.Reto3_Cabin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServiciosReservaciones {
    
    @Autowired
    private RepositorioReservaciones metodosCrud;
    
    public List<Reservaciones> getAll(){
        return metodosCrud.getAll();
    }
    
    public Optional<Reservaciones> getReservation(int reservationId){
        return metodosCrud.getReservation(reservationId);
    }
    
    public Reservaciones save(Reservaciones reservation) {
        if (reservation.getIdReservation() == null) {
            return metodosCrud.save(reservation);
        } else {
            Optional<Reservaciones> e = metodosCrud.getReservation(reservation.getIdReservation());
            if (e.isEmpty()) {
                return metodosCrud.save(reservation);
            } else {
                return reservation;
            }
        }
    }
    
    public Reservaciones update (Reservaciones reservation){
        if(reservation.getIdReservation()!=null){
            Optional <Reservaciones> e=metodosCrud.getReservation(reservation.getIdReservation());
            if(!e.isEmpty()){
                
                if(reservation.getStartDate()!=null){
                    e.get().setStartDate(reservation.getStartDate());
                }
                
                if(reservation.getDevolutionDate()!=null){
                    e.get().setDevolutionDate(reservation.getDevolutionDate());
                }
                
                if(reservation.getStatus()!=null){
                    e.get().setStatus(reservation.getStatus());
                }
                metodosCrud.save(e.get());
                return e.get();    
            }else{
                return reservation;
            }
        }else{
            return reservation;
        }
    }
    
    public boolean deleteReservation(int reservationId) {
        Boolean aBoolean = getReservation(reservationId).map(reservation -> {
            metodosCrud.delete(reservation);
            return true;
        }).orElse(false);
        return aBoolean;
    }
    // codigo 311021
    public StatusReservaciones getReporteStatusReservaciones(){
        List<Reservaciones>completed= metodosCrud.ReservacionesStatus("completed");
        List<Reservaciones>cancelled= metodosCrud.ReservacionesStatus("cancelled");
        return new StatusReservaciones (completed.size(), cancelled.size());
    }
    
    public List<Reservaciones> getReportesTiempoReservaciones(String datoA, String datoB){
        SimpleDateFormat parser=new SimpleDateFormat ("yyyy-MM-dd");
        Date datoUno = new Date();
        Date datoDos = new Date();
        
        try{
            datoUno = parser.parse(datoA);
            datoDos = parser.parse(datoB);
        
        }catch(ParseException evt){
            evt.printStackTrace();
        
        }if(datoUno.before(datoDos)){
            return metodosCrud.ReservacionesTiempo(datoUno, datoDos);
                    
        }else{
            return new ArrayList<>();
        }   
    }
    
    public List<ContadorClientes> serviciosTopClientes(){
        return metodosCrud.getTopClientes();
    }
    
    
}
