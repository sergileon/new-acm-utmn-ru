package olymp;

import org.junit.Test;
import ru.sibint.olymp.api.APIEndpoint;
import ru.sibint.olymp.util.EMailSender;

public class SendEmailTestCase {

	@Test
	public void testSendEmail() {
		EMailSender eMailSender = new EMailSender();
		eMailSender.SendEmail("107th@mail.ru", "475508th");
	}
	
}
