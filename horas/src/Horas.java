import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Horas {
    private static Integer horas = 0;
    private static Integer minutos = 0;

    public static String geraHorasPorExtenso(Integer valor){
        return EnumMeiaHora.findByCodigo(valor).getValor();
    }

    public static List<String> split(String str){
        if(!str.isEmpty()) {
            return Stream.of(str.split(":"))
                    .map(hm -> new String(hm))
                    .collect(Collectors.toList());
        }else{
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        System.out.println("Informe o Horário? Exemplo de formato: HH:mm / 03:00 ");
        String horário= leitor.nextLine();
        List<String> horasMinutos = split(horário);

        if(!horasMinutos.isEmpty()) {
            horas = Integer.parseInt(horasMinutos.get(0));
            minutos = Integer.parseInt(horasMinutos.get(1));

            if (minutos <= 1) {
                System.out.println(geraHorasPorExtenso(horas) + " hora(s)");
            } else if (minutos > 1 && minutos <= 30) {
                System.out.println(horário);
            } else {
                Integer falta = 60 - minutos;
                Integer proximaHora = horas + 1;
                System.out.println(geraHorasPorExtenso(falta) + " minuto(s) para as " + geraHorasPorExtenso(proximaHora) + " hora(s)");
            }
        }else{
            main(args);
        }
    }

}
