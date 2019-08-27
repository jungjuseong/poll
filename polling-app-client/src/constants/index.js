export const EC2 = 'http://ec2-15-164-218-77.ap-northeast-2.compute.amazonaws.com';
export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || EC2 + ':8080/api';
//export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';
export const ACCESS_TOKEN = 'accessToken';

export const POLL_LIST_SIZE = 30;
export const MAX_CHOICES = 6;
export const POLL_QUESTION_MAX_LENGTH = 140;
export const POLL_CHOICE_MAX_LENGTH = 40;

export const DOCUMENT_LIST_SIZE = 30;
export const DOCUMENT_NAME_MAX_LENGTH = 100;
export const DOCUMENT_PREFERENCE_MAX_LENGTH = 2048;
export const DOCUMENT_CONTENTS_MAX_LENGTH = 16000000;

export const FULLNAME_MIN_LENGTH = 2;
export const FULLNAME_MAX_LENGTH = 30;
export const EMAIL_MAX_LENGTH = 30;

export const USERNAME_MIN_LENGTH = 5;
export const USERNAME_MAX_LENGTH = 20;

export const PASSWORD_MIN_LENGTH = 5;
export const PASSWORD_MAX_LENGTH = 20;
