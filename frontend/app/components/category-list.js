import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
import { tracked } from '@glimmer/tracking';

const ICON_MAP = [
  { value: '🍔', label: 'Food' },
  { value: '🚗', label: 'Transport' },
  { value: '🛒', label: 'Shopping' },
  { value: '🏠', label: 'Rent' },
  { value: '💼', label: 'Salary' },
  { value: '💻', label: 'Freelance' },
  { value: '🎮', label: 'Entertainment' },
  { value: '💊', label: 'Health' },
  { value: '📚', label: 'Education' },
  { value: '🏷', label: 'Other' },
];

export default class CategoryListComponent extends Component {
  @service api;
  @tracked deletedIds = [];

  get categories() {
    const cats = this.args.categories || [];
    return cats.filter(c => !this.deletedIds.includes(c.id));
  }

  get expenseCategories() {
    return this.categories.filter(c => c.type === 'EXPENSE');
  }

  get incomeCategories() {
    return this.categories.filter(c => c.type === 'INCOME');
  }

  get hasCategories() {
    return this.categories.length > 0;
  }
  @action
  fetchIcon(iconName) {
    return ICON_MAP.find(icon => icon.label === iconName)?.value;
  }

  @action
  async deleteCategory(id) {
    if (confirm('Delete this category?')) {
      try {
        await this.api.deleteCategory(id);
        this.deletedIds = [...this.deletedIds, id];
      } catch (e) {
        alert('Failed to delete category');
      }
    }
  }
}