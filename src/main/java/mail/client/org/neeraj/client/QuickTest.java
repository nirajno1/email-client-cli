package mail.client.org.neeraj.client;

public class QuickTest {
	private static final Integer SHARJAH = 100003361;
	private static final Integer DUBAI = 100003358;
	
public static void main(String[] args) {
	Integer testCity=100002029;
	System.out.println(findresult(testCity));
}

private static boolean findresult(Integer testCity) {
	// TODO Auto-generated method stub
	return (!(DUBAI.equals(testCity) || SHARJAH.equals(testCity)));
}
}
