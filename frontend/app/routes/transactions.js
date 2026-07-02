import Route from '@ember/routing/route';
import { service } from '@ember/service';

export default class TransactionsRoute extends Route {
  @service auth;
  @service api;
  @service router;

  beforeModel() {
    if (!this.auth.isAuthenticated) {
      this.router.transitionTo('login');
    }
  }

  async model() {
    try {
      return await this.api.getTransactions();
    } catch (e) {
      return [];
    }
  }
}