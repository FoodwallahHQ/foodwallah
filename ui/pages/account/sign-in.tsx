import React from 'react';
import SignIn from "@/pages/SignIn";
import Head from "next/head";

export async function getServerSideProps(context) {
  return {
    props: {
      confirmationFailed: context.query.confirmationFailed ? context.query.confirmationFailed : false
    }
  }
}

function SignInIndex(props) {
  return (
    <>
      <Head>
        <title>Foodwallah | Sign In</title>
      </Head>
      <SignIn confirmationFailed={props.confirmationFailed}/>
    </>
  );
}


export default SignInIndex
