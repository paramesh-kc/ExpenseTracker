import Component from '@glimmer/component';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';
import { service } from '@ember/service';

const ICON_OPTIONS = [
  { value: '🍔', label: 'Food' },
  { value: '🚗', label: 'Transport' },
  { value: '🛒', label: 'Shopping' },
  { value: '🏠', label: 'Rent' },
  { value: '💼', label: 'Salary' },
  { value: '💻', label: 'Freelance' },
  { value: '🎮', label: 'Entertainment' },
  { value: '💊', label: 'Health' },
  { value: '📚', label: 'Education' },
  { value: '💰', label: 'Loans' },
  { value: '🏷', label: 'Other' }
];

export default class CategoryFormComponent extends Component {
  @service api;
  @service router;

  @tracked name = '';
  @tracked icon = '🍔';
  @tracked color = '#3498db';
  @tracked type = 'EXPENSE';
  @tracked error = '';
  @tracked isLoading = false;

  iconOptions = ICON_OPTIONS;

  @action updateName(e) { this.name = e.target.value; }
  @action updateIcon(e) { this.icon = e.target.value; }
  @action updateColor(e) { this.color = e.target.value; }
  @action updateType(e) { this.type = e.target.value; }

  @action
  async handleSubmit(e) {
    e.preventDefault();
    this.error = '';
    this.isLoading = true;

    try {
      await this.api.createCategory({
        name: this.name,
        icon: this.icon,
        color: this.color,
        type: this.type,
      });

      // Reset form
      this.name = '';
      this.icon = '🍔';
      this.color = '#3498db';
      this.type = 'EXPENSE';

      // Refresh route to reload categories list
      this.router.refresh();
    } catch (err) {
      this.error = err.errors?.error || 'Failed to create category';
    } finally {
      this.isLoading = false;
    }
  }
}