This application supports both username-password and oauth login via Google and GitHub.

The application uses environment variables for sensitive configuration values such as client IDs and secrets. These
variables should be set in the environment where the application is running. For example, in a Docker container, you can
use the `--env` flag to pass these variables, or in a Kubernetes deployment, you can define them as environment
variables in the pod spec.

Google login url: `http://localhost:8194/oauth2/authorization/google` <br>
Github login url: `http://localhost:8194/oauth2/authorization/github`

After verifying the login, the user is redirected to the application's home page with an access token in the URL query
parameters.