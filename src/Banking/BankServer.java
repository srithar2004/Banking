package Banking;

import fi.iki.elonen.NanoHTTPD;
import java.math.BigDecimal;
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

            if (Method.OPTIONS.equals(method)) {
                Response response = newFixedLengthResponse("");
                addCORSHeaders(response);
                return response;
            }

            Map<String, String> body = new HashMap<>();
            session.parseBody(body);
            Map<String, String> params = session.getParms();

            Response response;

            if (Method.POST.equals(method)) {
                switch (uri) {
                    case "/deposit": {
                        long accno = Long.parseLong(params.get("accno"));
                        BigDecimal amount = new BigDecimal(params.get("amount"));
                        response = newFixedLengthResponse(bankAccount.deposit(accno, amount));
                        break;
                    }

                    case "/withdraw": {
                        long acc = Long.parseLong(params.get("accno"));
                        BigDecimal take = new BigDecimal(params.get("amount"));
                        response = newFixedLengthResponse(bankAccount.withdraw(acc, take));
                        break;
                    }

                    case "/transfer": {
                        long from = Long.parseLong(params.get("from"));
                        long to = Long.parseLong(params.get("to"));
                        BigDecimal amt = new BigDecimal(params.get("amount"));
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

            addCORSHeaders(response);
            return response;

        } catch (NumberFormatException e) {
            Response response = newFixedLengthResponse("Invalid number format: " + e.getMessage());
            addCORSHeaders(response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            Response response = newFixedLengthResponse("Error: " + e.getMessage());
            addCORSHeaders(response);
            return response;
        }
    }

    private void addCORSHeaders(Response response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    public static void main(String[] args) throws Exception {
        new BankServer();
    }
}
