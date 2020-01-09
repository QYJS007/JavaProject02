package thread.example.ticket;

public class Station extends Thread {



	public Station(String name ) {
		super(name);
	}

	public void run() {
		while( Ticket.ticket>0){
			new Ticket().sellTicket();;
		}
	}

}
