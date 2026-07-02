import Component from '@glimmer/component';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';
import { service } from '@ember/service';
export default class LoginFormComponent extends Component {
    @service api;
    @service auth;
    @service router;
    @tracked email = '';
    @tracked password = '';
    @tracked error = '';
    @tracked isLoading = false;
    @action updateEmail(e) { this.email = e.target.value; }
    @action updatePassword(e) { this.password = e.target.value; }
    @action
    async handleSubmit(e) {
        e.preventDefault();
        this.error = '';
        this.isLoading = true;
        try {
            const data = await this.api.login(this.email, this.password);
            this.auth.setUser(data);
            this.router.transitionTo('dashboard');
        } catch (err) {
            this.error = err.errors?.error || 'Login failed';
        } finally {
            this.isLoading = false;
        }
    }
}