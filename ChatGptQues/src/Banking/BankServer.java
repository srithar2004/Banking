package Banking;

import fi.iki.elonen.NanoHTTPD;
import java.util.HashMap;
import java.util.Map;

public class BankServer extends NanoHTTPD {

    private BankAccount bankAccount = new BankAccount();
    private Bank bank = new Bank();

    public BankServer() throws Exception {
        super(8083); 
        start(SOCKET_READ_TIMEOUT, false);
        System.out.println("✅ Connected to MySQL Database");
        System.out.println("✅ Server started at http://localhost:8083");
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            String uri = session.getUri();
            Method method = session.getMethod();

            // ✅ Step 1: Handle preflight (CORS) request from browser
            if (Method.OPTIONS.equals(method)) {
                Response response = newFixedLengthResponse("");
                addCORSHeaders(response);
                return response;
            }

            // ✅ Step 2: Parse request body safely
            Map<String, String> body = new HashMap<>();
            session.parseBody(body);
            Map<String, String> params = session.getParms();

            Response response;

            // ✅ Step 3: Handle routes
            if (Method.POST.equals(method)) {
                switch (uri) {
                    case "/deposit": {
                        long accno = Long.parseLong(params.get("accno"));
                        long amount = Long.parseLong(params.get("amount"));
                        response = newFixedLengthResponse(bankAccount.deposit(accno, amount));
                        break;
                    }

                    case "/withdraw": {
                        long acc = Long.parseLong(params.get("accno"));
                        long take = Long.parseLong(params.get("amount"));
                        response = newFixedLengthResponse(bankAccount.withdraw(acc, take));
                        break;
                    }

                    case "/transfer": {
                        long from = Long.parseLong(params.get("from"));
                        long to = Long.parseLong(params.get("to"));
                        long amt = Long.parseLong(params.get("amount"));
                        response = newFixedLengthResponse(bank.TransferAmount(from, to, amt));
                        break;
                    }

                    default:
                        response = newFixedLengthResponse("Unknown POST endpoint: " + uri);
                }
            } else if (Method.GET.equals(method) && uri.startsWith("/account")) {
                String accParam = params.get("accno");
                if (accParam == null)
                    response = newFixedLengthResponse("Missing accno parameter");
                else {
                    long accno = Long.parseLong(accParam);
                    response = newFixedLengthResponse(bank.DisplayAccDetails(accno));
                }
            } else {
                response = newFixedLengthResponse("Welcome to Banking API!");
            }

            // ✅ Step 4: Add CORS headers to all responses
            addCORSHeaders(response);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            Response response = newFixedLengthResponse("Error: " + e.getMessage());
            addCORSHeaders(response);
            return response;
        }
    }

    // ✅ Step 5: Helper method to allow frontend access
    private void addCORSHeaders(Response response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    public static void main(String[] args) throws Exception {
        new BankServer();
    }
}
