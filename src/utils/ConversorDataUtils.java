package utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ConversorDataUtils {

	public static String getTimeToString(LocalDateTime horario) {
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("HH:mm");
		String novoHorario = formatador.format(horario);
		return novoHorario;
	}

	public static String getDateTimeToString(LocalDateTime dateTime) {
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String novaDateTime = formatador.format(dateTime);
		return novaDateTime;
	}

	public static Date getDateTimeToDate(LocalDateTime dateTime) {
		Date novaDateTime = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		return novaDateTime;
	}
}
