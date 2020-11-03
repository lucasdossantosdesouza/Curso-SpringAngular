import java.util.AbstractMap;
import java.util.Map;
import java.util.Scanner;

public class Horas {
    private static Integer horas = 0;
    private static Integer minutos = 0;

    private static Map<Integer, String> mapUnidade = Map.ofEntries(
            new AbstractMap.SimpleEntry<Integer, String>(0, ""),
            new AbstractMap.SimpleEntry<Integer, String>(1, "um"),
            new AbstractMap.SimpleEntry<Integer, String>(2, "dois"),
            new AbstractMap.SimpleEntry<Integer, String>(3, "três"),
            new AbstractMap.SimpleEntry<Integer, String>(4, "quatro"),
            new AbstractMap.SimpleEntry<Integer, String>(5, "cinco"),
            new AbstractMap.SimpleEntry<Integer, String>(6, "seis"),
            new AbstractMap.SimpleEntry<Integer, String>(7, "sete"),
            new AbstractMap.SimpleEntry<Integer, String>(8, "oito"),
            new AbstractMap.SimpleEntry<Integer, String>(9, "nove"),
            new AbstractMap.SimpleEntry<Integer, String>(10, "dez"),
            new AbstractMap.SimpleEntry<Integer, String>(11, "onze"),
            new AbstractMap.SimpleEntry<Integer, String>(12, "doze"),
            new AbstractMap.SimpleEntry<Integer, String>(13, "treze"),
            new AbstractMap.SimpleEntry<Integer, String>(14, "quatorze"),
            new AbstractMap.SimpleEntry<Integer, String>(15, "quinze"),
            new AbstractMap.SimpleEntry<Integer, String>(16, "dezesseis"),
            new AbstractMap.SimpleEntry<Integer, String>(17, "dezessete"),
            new AbstractMap.SimpleEntry<Integer, String>(18, "dezoito"),
            new AbstractMap.SimpleEntry<Integer, String>(19, "dezenove"),
            new AbstractMap.SimpleEntry<Integer, String>(20, "vinte"),
            new AbstractMap.SimpleEntry<Integer, String>(21, "vinte e um"),
            new AbstractMap.SimpleEntry<Integer, String>(22, "vinte e dois"),
            new AbstractMap.SimpleEntry<Integer, String>(23, "vinte e três"),
            new AbstractMap.SimpleEntry<Integer, String>(24, "vinte e quatro"),
            new AbstractMap.SimpleEntry<Integer, String>(25, "vinte e cinco"),
            new AbstractMap.SimpleEntry<Integer, String>(26, "vinte e seis"),
            new AbstractMap.SimpleEntry<Integer, String>(27, "vinte e sete"),
            new AbstractMap.SimpleEntry<Integer, String>(28, "vinte e oito"),
            new AbstractMap.SimpleEntry<Integer, String>(29, "vinte e nove")
    );

    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        System.out.println("Informe o Horário? Exemplo de formato: HH:mm / 03:00 ");
        String horário= leitor.nextLine();

        horas = Integer.parseInt(horário.split(":")[0]);
        minutos = Integer.parseInt(horário.split(":")[1]);

        if(minutos <=1 ){
            System.out.println(mapUnidade.get(horas) +" hora(s)");
        }else if(minutos > 1 && minutos <= 30){
            System.out.println(horário);
        }else{
            Integer falta = 60 - minutos;
            Integer proximaHora = horas + 1;
            System.out.println(mapUnidade.get(falta)+ " minuto(s) para as "+ mapUnidade.get(proximaHora) +" hora(s)" );
        }
    }
}
