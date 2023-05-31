const CracoLessPlugin = require('craco-less');

module.exports = {
  webpack: {
    configure: (webpackConfig, { env }) => {
      if (env === 'production') {
        // Production ortamında sadece source map'leri devre dışı bırakın.
        webpackConfig.devtool = false;
      }
      return webpackConfig;
    },
  },
  plugins: [
    {
      plugin: CracoLessPlugin,
      options: {
        lessLoaderOptions: {
          lessOptions: {
            modifyVars: {
              '@primary-color': '#3B97D2',
              '@link-color': '#3B97D2',
              '@border-radius-base': '8px',
              '@input-border-color': 'transparent',
              '@select-border-color': 'transparent',
              '@input-number-handler-border-color': 'transparent',
              '@heading-color': '#5c5c5c',
              '@text-color': '#5c5c5c',
              '@disabled-bg': '#E9E9E9',
              '@font-size-lg': '14px',
              '@modal-header-bg': '#E9E9E9',
              '@modal-content-bg': '#F6F6F7',
              '@modal-footer-bg': '#E9E9E9',
              '@table-row-hover-bg': '#FFFFFF',
            },
            javascriptEnabled: true,
          },
        },
      },
    },
  ],
};
