import React, { Component } from 'react';

import { Input,Avatar } from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { formatDateTime } from '../util/Helpers';

import './Document.css';

const { TextArea } = Input

class Document extends Component {

    render() {     

        console.log('this.props.document:');
        console.log(this.props.document);

        if (this.props.document.createdBy == null) {
            return null;
        }
        let bgColor = getAvatarColor(this.props.document.createdBy.fullname);
        bgColor = (this.props.document.deadmark === 'y') ? bgColor ^ 0xff00ff : bgColor;

        return (
            <div className="document-content">
                <div className="document-header">
                    <div className="document-name">
                        {this.props.document.name}
                    </div>
                    <div className="document-creator-info">
                        <Link className="creator-link" to={`/users/${this.props.document.createdBy.username}`}>
                            <Avatar className="document-creator-avatar" 
                                style={{ backgroundColor: bgColor}} >
                                {this.props.document.createdBy.fullname.toUpperCase()}
                            </Avatar>
                            <span className="document-creator-name">
                                {this.props.document.createdBy.fullname}
                            </span>
                            <span className="document-creator-username">
                                @{this.props.document.createdBy.username}
                            </span>
                            <span className="document-creation-date">
                                {formatDateTime(this.props.document.creationDateTime)} 
                            </span>
                        </Link>
                    </div>

                    <div className="document-name">
                        <TextArea 
                            style = {{ fontSize: '12px' }} 
                            autosize={{ minRows: 3, maxRows: 10 }} 
                            name = "contents"
                            value = {this.props.document.contents}
                         />
                    </div>
                    <div className="document-name">
                        <TextArea 
                            style = {{ fontSize: '12px' }} 
                            autosize={{ minRows: 3, maxRows: 10 }} 
                            name = "preference"
                            value = {this.props.document.preference}
                        />
                    </div>

                </div>
            </div>
        );
    }
}

export default Document;