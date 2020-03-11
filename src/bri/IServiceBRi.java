/**
 * Interface dédiée aux services BRi
 * @author VO Thierry & RISI Lucas
 * @version 1.0
 */

package bri;

import java.net.Socket;

public interface IServiceBRi extends Runnable {

	void setSocket(Socket s);
	
}
