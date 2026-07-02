import { helper } from '@ember/component/helper';

export default helper(function lowerCase([value]) {
  return value ? value.toLowerCase() : '';
});