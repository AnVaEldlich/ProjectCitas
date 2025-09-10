package Modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GestorPaciente {

    private final List<Paciente> pacientes;

    public GestorPaciente() {
        this.pacientes = new LinkedList<>();
    }

    public void registrarPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }

    public List<Paciente> buscarPacientesPorParametro(Parametro parametro, String valor) {
        return pacientes.stream()
                .filter(pac -> switch (parametro) {
                    case IDENTIFICACION -> pac.getIdentificacion().equalsIgnoreCase(valor);
                    case NOMBRES -> pac.getNombres().equalsIgnoreCase(valor);
                    case APELLIDOS -> pac.getApellidos().equalsIgnoreCase(valor);
                    case GENERO -> pac.getGenero().equalsIgnoreCase(valor);
                })
                .collect(Collectors.toList());
    }

    public void RegistrarPacientes(Paciente pacienteModelo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Enum for parameters instead of magic numbers
    public enum Parametro {
        IDENTIFICACION,
        NOMBRES,
        APELLIDOS,
        GENERO
    }
}



/*
package Modelo;
 import java.util.*;
 /**
 *
 * @author Usuario
 /*
 public class GestorPaciente {
    private static LinkedList<Paciente> pacientes;
    public GestorPaciente(){
        pacientes=new LinkedList<Paciente>();
    }
    public void RegistrarPacientes(Paciente paciente){
    pacientes.add(paciente);
    }
 public static LinkedList<Paciente> getPacientebyParametro(int parametro, String valor)
   {
      LinkedList<Paciente> resultado=new LinkedList<Paciente>();
      for(Paciente pac:pacientes)
      {
          switch(parametro){
              case 1: if(pac.getIdentificacion().equals(valor))
                      resultado.add(pac);
                      break;
              case 2: if(pac.getNombres().equals(valor))
                      resultado.add(pac);
                      break;
              case 3: if(pac.getApellidos().equals(valor))
                      resultado.add(pac);
                      break;
              case 4: if(pac.getGenero().equals(valor))
                      resultado.add(pac);
                      break;
          }
   } 

*/