import React, {FC} from "react";
import moment from "moment";
import {DocumentDetail} from "@/types/types";

interface DetailViewProps {
  post?: DocumentDetail
}

const DetailView: FC<DetailViewProps> = (props) => {
  let date = moment(props.post?.createdAt * 1000).format('ll')
    return (
        <div className="blog-detail-container">
          <article className="container px-2 mx-auto mb4" itemScope={true}
                   itemType="http://schema.org/BlogPosting">
            <h1 className="h0-blog-detail col-12 sm-width-full py-4 mt-3 inline-block"
                itemProp="name headline">{props.post?.title}</h1>
            <div className="col-4 sm-width-full mt-1 border-top-thin ">
              <p className="mb-3 py-2 bold h4">
                <time dateTime={date} itemProp="datePublished">{date}</time>
              </p>
            </div>
            <div dangerouslySetInnerHTML={{__html: props.post?.content}} className="prose" itemProp="articleBody">
            </div>
          </article>

          <div className="container mx-auto px-2 py-2 clearfix">

            {
              props.post?.prev && (
                    <div className="col-4 sm-width-full left mr-lg-4 mt-3">
                      <a className="no-underline border-top-thin py-1 block"
                         href={"/post/" + props.post.prev.slug}>
                        <span className="h5 bold text-accent">Previous</span>
                        <p className="bold h3 link-primary mb-1">{props.post.prev.title}</p>
                        <p>{props.post.prev.description}</p>
                      </a>
                    </div>
                )
            }

            {
              props.post?.next && (
                    <div className="col-4 sm-width-full left mt-3">
                      <a className="no-underline border-top-thin py-1 block"
                         href={"/post/" + props.post.next.slug}>
                        <span className="h5 bold text-accent">Next</span>
                        <p className="bold h3 link-primary mb-1">{props.post.next.title}</p>
                        <p>{props.post.next.description}</p>
                      </a>
                    </div>
                )
            }

          </div>

        </div>
    );
}

export default DetailView;