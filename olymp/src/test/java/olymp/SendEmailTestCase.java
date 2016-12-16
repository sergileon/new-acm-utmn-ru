package olymp;

import org.junit.Test;
import ru.sibint.olymp.api.APIEndpoint;

public class SendEmailTestCase {

	@Test
	public void testSendEmail() {
		APIEndpoint ae = new APIEndpoint();
		ae.SendEmail("107th@mail.ru", "475508th");
	}
	
}
