import React, { Component } from 'react';
import { 
    getAllDocuments, 
    getUserCreatedDocuments, 
    getDocumentsUserCreatedAndName } from '../util/APIUtils';
import Document from './Document';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Icon } from 'antd';
import { DOCUMENT_LIST_SIZE } from '../constants';
import { withRouter } from 'react-router-dom';

import './DocumentList.css';

class DocumentList extends Component {
    constructor(props) {
        super(props);

        this.state = {
            documents: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            isLoading: false
        };
        // this.loadDocumentList = this.loadDocumentList.bind(this);
        // this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    loadDocumentList = (page = 0, size = DOCUMENT_LIST_SIZE) => {

        let promise = (this.props.username) ? 
            getUserCreatedDocuments(this.props.username, page, size) :
            getDocumentsUserCreatedAndName('changhee', 'untitled', page, size, 'updatedAt', 'desc');
            // getAllDocuments(page, size);
    
        if(!promise) {
            return;
        }
        this.setState({
            isLoading: true
        });

        promise            
        .then(response => {
            const documents = this.state.documents.slice();

            this.setState({
                documents: documents.concat(response.content),
                page: response.page,
                size: response.size,
                totalElements: response.totalElements,
                totalPages: response.totalPages,
                last: response.last,
                isLoading: false
            })
        }).catch(error => {
            this.setState({
                isLoading: false
            })
        });  
        
    }

    componentDidMount = () => {
        this.loadDocumentList();
    }

    componentDidUpdate = (nextProps) => {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                documents: [],
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                isLoading: false
            });    
            this.loadDocumentList();
        }
    }

    handleLoadMore = () => {
        this.loadDocumentList(this.state.page + 1);
    }

    render() {
        const views = [];
        this.state.documents.forEach((document, index) => {
            views.push(
                <Document key={document.id} document={document}/>                            
            );
        });

        

        return (
            <div className="documents-container">
                {views}
                {
                    !this.state.isLoading && this.state.documents.length === 0 ? (
                        <div className="no-documents-found">
                            <span>No Documents Found.</span>
                        </div>    
                    ): null
                }  
                {
                    !this.state.isLoading && !this.state.last ? (
                        <div className="load-more-documents"> 
                            <Button type="dashed" onClick={this.handleLoadMore} disabled={this.state.isLoading}>
                                <Icon type="plus" /> Load more
                            </Button>
                        </div>): null
                }              
                {
                    this.state.isLoading ? 
                    <LoadingIndicator />: null                     
                }
            </div>
        );
    }
}

export default withRouter(DocumentList);