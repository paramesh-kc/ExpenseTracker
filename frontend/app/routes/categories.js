import Route from '@ember/routing/route';
import { service } from '@ember/service';

export default class CategoriesRoute extends Route {
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
      let getCategoriesDetails = await this.api.getCategories();
      console.log("getCategoriesDetails", getCategoriesDetails);
      return getCategoriesDetails;
    } catch (e) {
      return [];
    }
  }
}