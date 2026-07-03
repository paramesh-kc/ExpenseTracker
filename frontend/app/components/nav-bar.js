import Component from '@glimmer/component';
import { service } from '@ember/service';
import { action } from '@ember/object';

export default class NavBarComponent extends Component {
  @service auth;
  @service api;
  @service router;

  @action
  async logout() {
    try {
      await this.api.logout();     // Server deletes the cookie
    } catch (e) {
      // logout anyway even if API fails
    }
    this.auth.logout();            // Clear local display info
    this.router.transitionTo('login');
  }

  @action
  navigate(routeName, event) {
    event.preventDefault();
    this.router.transitionTo(routeName).then(() => {
      this.router.refresh();
    });
  }
}