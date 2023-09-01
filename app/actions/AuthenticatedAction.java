package actions;

import models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.EmployeeDatabase;
import services.JWTService;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class AuthenticatedAction extends Action<Security.Authenticated> {
    private final EmployeeDatabase database;
    @Autowired
    private JWTService jwtService;

    @Inject
    public AuthenticatedAction(EmployeeDatabase database, JWTService jwtService) {
        this.database = database;
        this.jwtService = jwtService;
    }

    @Override
    public CompletionStage<Result> call(Http.Request request) {
        String authHeader = request.header("Authorization").toString();
        String token;
        String userName1 = null;
        System.out.println("authHeader " + authHeader);
        if (authHeader != null && authHeader.startsWith("Optional[Bearer")) {
            token = authHeader.substring(16, 145);
            System.out.println("token " + token);
            userName1 = jwtService.extractUsername(token);

        } else {
            token = null;
        }

        if (userName1 != null) {
            CompletableFuture<Optional<?>> futureUserAccount = database.loadUserByUsername(userName1);
            return futureUserAccount.thenCompose(userAccountOptional -> {
                if (userAccountOptional.isPresent()) {
                    UserAccount userAccount = (UserAccount) userAccountOptional.get();
                    if (jwtService.validateToken(token, userAccount)) {
                        return delegate.call(request);
                    }else {
                        return CompletableFuture.completedFuture(unauthorized("Unauthorized"));
                    }
                }
                return CompletableFuture.completedFuture(unauthorized("Unauthorized"));
            });
        }
        return CompletableFuture.completedFuture(unauthorized("Unauthorized"));
    }
}
