export const required = (value) => {
  if (!value || (typeof value === 'string' && !value.trim())) {
    return 'Trường này không được để trống';
  }
  return null;
};

export const minLength = (min) => (value) => {
  if (value && value.length < min) {
    return `Tối thiểu ${min} ký tự`;
  }
  return null;
};

export const isEmail = (value) => {
  if (value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
    return 'Email không hợp lệ';
  }
  return null;
};

export const isPhone = (value) => {
  if (value && !/^(0[35789])\d{8}$/.test(value)) {
    return 'Số điện thoại không hợp lệ';
  }
  return null;
};

export const isNumber = (value) => {
  if (value && isNaN(value)) {
    return 'Phải là số';
  }
  return null;
};

export const validate = (value, ...validators) => {
  for (const validator of validators) {
    const error = validator(value);
    if (error) return error;
  }
  return null;
};
