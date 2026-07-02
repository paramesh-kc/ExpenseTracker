import Controller from '@ember/controller';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';

const ICON_OPTIONS = [
  { value: 'ti-tools-kitchen-2', label: 'Food' },
  { value: 'ti-car', label: 'Transport' },
  { value: 'ti-shopping-bag', label: 'Shopping' },
  { value: 'ti-home', label: 'Rent' },
  { value: 'ti-briefcase', label: 'Salary' },
  { value: 'ti-laptop', label: 'Freelance' },
  { value: 'ti-tag', label: 'Other' },
];

export default class CategoriesController extends Controller {
  @service store;
  @service auth;

  @tracked showForm = false;
  @tracked name = '';
  @tracked type = 'EXPENSE';
  @tracked icon = 'ti-tag';
  @tracked color = '#4f6df5';
  @tracked errorMessage = '';
  @tracked isSubmitting = false;
  @tracked localCategories = null;

  iconOptions = ICON_OPTIONS;

  get categoriesList() {
    return this.localCategories || this.model;
  }

  get expenseCategories() {
    return this.categoriesList.filter((c) => c.type === 'EXPENSE');
  }

  get incomeCategories() {
    return this.categoriesList.filter((c) => c.type === 'INCOME');
  }

  get userId() {
    return this.auth.user?.id || 1;
  }

  @action
  toggleForm() {
    this.showForm = !this.showForm;
    this.errorMessage = '';
  }

  @action
  updateName(e) {
    this.name = e.target.value;
  }

  @action
  updateType(e) {
    this.type = e.target.value;
  }

  @action
  updateIcon(e) {
    this.icon = e.target.value;
  }

  @action
  updateColor(e) {
    this.color = e.target.value;
  }

  @action
  async createCategory(event) {
    event.preventDefault();
    this.errorMessage = '';
    this.isSubmitting = true;

    try {
      const created = await this.store.ajax(
        `http://localhost:8080/api/categories?userId=${this.userId}`,
        {
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify({
            name: this.name,
            icon: this.icon,
            color: this.color,
            type: this.type,
          }),
        }
      );

      this.localCategories = [...this.categoriesList, created];

      this.name = '';
      this.icon = 'ti-tag';
      this.color = '#4f6df5';
      this.showForm = false;
    } catch (err) {
      this.errorMessage =
        err.payload?.errors?.[0] || err.payload?.error || 'Could not create category';
    } finally {
      this.isSubmitting = false;
    }
  }

  @action
  async deleteCategory(category) {
    try {
      await this.store.ajax(
        `http://localhost:8080/api/categories/${category.id}?userId=${this.userId}`,
        { type: 'DELETE' }
      );
      this.localCategories = this.categoriesList.filter((c) => c.id !== category.id);
    } catch (err) {
      this.errorMessage = err.payload?.error || 'Could not delete category';
    }
  }
}