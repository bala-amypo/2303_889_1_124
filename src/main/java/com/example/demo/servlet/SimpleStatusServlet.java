@WebServlet("/status")
public class SimpleStatusServlet extends HttpServlet {
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
throws IOException {
resp.getWriter().write("OK");
}
}