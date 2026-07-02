import Component from '@glimmer/component';
import { service } from '@ember/service';
import { action } from '@ember/object';

export default class NavBarComponent extends Component {
  @service auth;
  @service router;

  @action
  logout() {
    this.auth.logout();
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