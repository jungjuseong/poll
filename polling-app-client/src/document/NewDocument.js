import React, { Component } from 'react';

import { createDocument } from '../util/APIUtils';
import { DOCUMENT_NAME_MAX_LENGTH, DOCUMENT_CONTENTS_MAX_LENGTH, DOCUMENT_PREFERENCE_MAX_LENGTH } from '../constants';
import { Form, Input, Button, notification, Checkbox } from 'antd';
import './NewDocument.css';  

const FormItem = Form.Item;
const { TextArea } = Input

class NewDocument extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: {
                text: ''
            },
            contents: {
                text: ''
            },
            preference: {
                text: ''
            },
            deadmark: {
                text: 'n'
            }
        };
        // this.handleSubmit = this.handleSubmit.bind(this);
        // this.handleNameChange = this.handleNameChange.bind(this);
        // this.handleContentsChange = this.handleContentsChange.bind(this);
        // this.handleDeadmarkChange = this.handleDeadmarkChange.bind(this);
        // this.isFormInvalid = this.isFormInvalid.bind(this);
    }

    handleSubmit = (event) => {
        event.preventDefault();
        const document = {
            name: this.state.name.text,
            contents: this.state.contents.text,
            preference: this.state.preference.text,
            deadmark: this.state.deadmark.text
        };

        createDocument(document) 
        .then(response => {
            console.log(response);

            this.props.history.push("/");
        }).catch(error => {
            if(error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login to create document.');    
            } else {
                notification.error({
                    message: 'PageBuilder Service',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });              
            }
        });
    }

    validateName = (name) => {
        if(name.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter the document name!'
            }
        } else if (name.length > DOCUMENT_NAME_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Document Name is too long (Maximum ${DOCUMENT_NAME_MAX_LENGTH} characters allowed)`
            }    
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }
    validateDeadmark = (mark) => {
        if (mark !== 'y' && mark !== 'n') {
            return {
                validateStatus: 'error',
                errorMsg: `Deadmark is 'y' or 'n' only)`
            }    
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }

    validateContents = (contents) => {
        if (contents.length > DOCUMENT_CONTENTS_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Contents is too long (Maximum ${DOCUMENT_CONTENTS_MAX_LENGTH} characters allowed)`
            }    
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }

    handleNameChange = (event) => {
        const value = event.target.value;
        this.setState({
            name: {
                text: value,
                ...this.validateName(value)
            }
        });
    }

    handleContentsChange = (event) => {
        const value = event.target.value;
        this.setState({
            contents: {
                text: value,
                ...this.validateContents(value)
            }
        });
    }

    validatePreference = (preference) => {
        if (preference.length > DOCUMENT_PREFERENCE_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Preference is too long (Maximum ${DOCUMENT_PREFERENCE_MAX_LENGTH} characters allowed)`
            }    
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }

    handlePreferenceChange = (event) => {
        const value = event.target.value;
        this.setState({
            preference: {
                text: value,
                ...this.validatePreference(value)
            }
        });
    }

    handleDeadmarkChange = (event) => {
        const value = event.target.checked;
        this.setState({
            deadmark: {
                text: (value) ? 'y' : 'n',
            }
        });
    }

    isFormInvalid = () => {
        if(this.state.name.validateStatus !== 'success') {
            return true;
        }
    }

    render() {

        return (
            <div className="new-poll-container">
                <h1 className="page-title">Create Document</h1>
                <div className="new-poll-content">
                    <Form onSubmit={this.handleSubmit} className="create-poll-form">
                        <FormItem validateStatus={this.state.name.validateStatus}
                            help={this.state.name.errorMsg} className="poll-form-row">
                            <Input                                 placeholder="Enter your document name"
                                style = {{ fontSize: '14px' }} 
                                name = "name"
                                value = {this.state.name.text}
                                onChange = {this.handleNameChange} />
                        </FormItem>

                        <FormItem validateStatus={this.state.contents.validateStatus}
                            help={this.state.contents.errorMsg} className="poll-form-row">
                            <TextArea placeholder="Enter your contents - JSON"
                                style = {{ fontSize: '14px' }} 
                                autosize={{ minRows: 3, maxRows: 10 }} 
                                name = "contents"
                                value = {this.state.contents.text}
                                onChange = {this.handleContentsChange} />
                        </FormItem>

                        <FormItem validateStatus={this.state.preference.validateStatus}
                            help={this.state.preference.errorMsg} className="poll-form-row">
                            <TextArea placeholder="Enter preference - JSON"
                                style = {{ fontSize: '14px' }} 
                                autosize={{ minRows: 2, maxRows: 10 }} 
                                name = "preference"
                                value = {this.state.preference.text}
                                onChange = {this.handlePreferenceChange} />
                        </FormItem>

                        <FormItem>
                            <Checkbox name = "deadmark" onChange = {this.handleDeadmarkChange} />
                             &nbsp; dead mark
                        </FormItem> 

                        <FormItem className="poll-form-row">
                            <Button type="primary" htmlType="submit" size="large" disabled={this.isFormInvalid()}
                                className="create-poll-form-button">Create Document</Button>
                        </FormItem>
                    </Form>
                </div>    
            </div>
        );
    }
}

export default NewDocument;