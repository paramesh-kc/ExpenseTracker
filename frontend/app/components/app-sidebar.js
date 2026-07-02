import Component from '@glimmer/component';
import { inject as service } from '@ember/service';

export default class AppSidebarComponent extends Component {
  @service auth;

  get initials() {
    const name = this.auth.user?.name || '';
    return name
      .split(' ')
      .map((p) => p[0])
      .join('')
      .slice(0, 2)
      .toUpperCase();
  }
}