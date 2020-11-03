import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;

public enum EnumMeiaHora {

   ZERO(0, ""),
   UM(1, "um"),
   DOIS(2, "dois"),
   TRES(3, "três"),
   QUATRO(4, "quatro"),
   CINCO(5, "cinco"),
   SEIS(6, "seis"),
   SETE(7, "sete"),
   OITO(8, "oito"),
   NOVE(9, "nove"),
   DEZ(10, "dez"),
   ONZE(11, "onze"),
   DOZE(12, "doze"),
   TREZE(13, "treze"),
   QUATORZE(14, "quatorze"),
   QUINZE(15, "quinze"),
   DEZESSEIS(16, "dezesseis"),
   DEZESSETE(17, "dezessete"),
   DEZOITO(18, "dezoito"),
   DEZENOVE(19, "dezenove"),
   VINTE(20, "vinte"),
   VINTEEUM(21, "vinte e um"),
   VINTEEDOIS(22, "vinte e dois"),
   VINTEETRES(23, "vinte e três"),
   VINTEEQUATRO(24, "vinte e quatro"),
   VINTEECINCO(25, "vinte e cinco"),
   VINTEESEIS(26, "vinte e seis"),
   VINTEESETE(27, "vinte e sete"),
   VINTEEOITO(28, "vinte e oito"),
   VINTEENOVE(29, "vinte e nove"),
   TRINTA(30, "trinta");

   private final Integer codigo;

   private final String valor;

   EnumMeiaHora(Integer codigo, String valor) {
      this.codigo = codigo;
      this.valor = valor;
   }

   public static EnumMeiaHora findByCodigo(Integer codigo){
      return Arrays.stream(EnumMeiaHora.values()).filter(enumMeiaHora ->! enumMeiaHora.equals(ZERO)
              && enumMeiaHora.codigo.equals(codigo) ).findAny().get();
   }

   public Integer getCodigo() {
      return codigo;
   }

   public String getValor() {
      return valor;
   }
}
