import Route from '@ember/routing/route';
import { service } from '@ember/service';
export default class ApplicationRoute extends Route {
    @service auth;
    @service router;
    beforeModel() {
    // Redirect to login if not authenticated
        const publicRoutes = ['login', 'register'];
        const currentRoute = this.router.currentRouteName;
    // Auth check happens in each protected route instead
    }
}