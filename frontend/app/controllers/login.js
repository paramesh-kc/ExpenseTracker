import Controller from '@ember/controller';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';

export default class LoginController extends Controller {
  @service store;
  @service auth;
  @service router;

  @tracked email = '';
  @tracked password = '';
  @tracked errorMessage = '';
  @tracked isSubmitting = false;

  @action
  updateEmail(event) {
    this.email = event.target.value;
  }

  @action
  updatePassword(event) {
    this.password = event.target.value;
  }

  @action
  async submit(event) {
    event.preventDefault();
    this.errorMessage = '';
    this.isSubmitting = true;

    try {
      const json = await this.store.ajax(
        'http://localhost:8080/api/auth/login',
        {
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify({ email: this.email, password: this.password }),
        }
      );
      this.auth.setSession(json);
      this.router.transitionTo('dashboard');
    } catch (errorObj) {
      this.errorMessage = errorObj.payload?.error || 'Invalid email or password';
    } finally {
      this.isSubmitting = false;
    }
  }
}