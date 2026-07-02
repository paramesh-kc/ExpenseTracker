import Store from '@ember-data/store';

export default class AppStore extends Store {
  async ajax(url, options = {}) {
    const response = await fetch(url, {
      method: options.type || 'GET',
      headers: { 'Content-Type': options.contentType || 'application/json' },
      body: options.data,
    });
    const json = await response.json();
    if (!response.ok) {
      const err = new Error(json.error || 'Request failed');
      err.payload = json;
      throw err;
    }
    return json;
  }
}